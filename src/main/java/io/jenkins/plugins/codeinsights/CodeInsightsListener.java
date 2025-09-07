/*
 * Copyright 2025 Nikolas Falco
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.jenkins.plugins.codeinsights;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.codeinsights.api.CodeInsightsReporBuilder;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenkinsci.plugins.displayurlapi.DisplayURLProvider;

/**
 * Sends CodeInsights report to Bitbucket Cloud on Run completed.
 */
@Extension
public class CodeInsightsListener extends RunListener<Run<?, ?>> {
    private static final Logger logger = Logger.getLogger(CodeInsightsListener.class.getName());
    private SCMFacade scmFacade;

    public CodeInsightsListener() {
        scmFacade = new SCMFacade();
    }

    @Override
    public void onCompleted(Run<?, ?> run, TaskListener listener) {
        CodeInsightsContext context = CodeInsightsContext.fromRun(run, DisplayURLProvider.get(), scmFacade);
        if (!context.isValid(listener)) {
            return;
        }

        try (CodeInsightsPublisher publisher = new CodeInsightsPublisher(context)) {
            ExtensionList<CodeInsightsReporBuilder> builders = ExtensionList.lookup(CodeInsightsReporBuilder.class);
            builders.forEach(builder -> {
                builder.build(run, context).ifPresent(report -> {
                    try {
                        publisher.publish(report);
                    } catch (IOException e) {
                        listener.error("Fail to publish code insights {0}", report.getReportType());
                        logger.log(Level.SEVERE, e, () -> "Fail to publish code insights " + report.getReportType());
                    }
                });
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> "Fail to close bitbucket client");
        }
    }
}
