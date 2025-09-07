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
package io.jenkins.plugins.codeinsights.api;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.ExtensionPoint;
import hudson.model.Run;
import io.jenkins.plugins.codeinsights.CodeInsightsContext;
import io.jenkins.plugins.codeinsights.api.dto.Report;
import java.util.Optional;

/**
 * Extensions point that provide a code insights report to publish on Bitbucket
 * Cloud extracting informations from a running build.
 */
public interface CodeInsightsReporBuilder extends ExtensionPoint {

    /**
     * Build a {@link Report} to publish on Bitbucket Cloud.
     *
     * @param run running build of a job
     * @param context build to gather useful information from given build run
     * @return a report if the build has the required info or {@code empty} is
     *         it is not.
     */
    @NonNull
    Optional<Report> build(Run<?, ?> run, CodeInsightsContext context);
}
