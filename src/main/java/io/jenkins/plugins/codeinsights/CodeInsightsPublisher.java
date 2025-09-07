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

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSource;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketApi;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketAuthenticatedClient;
import com.damnhandy.uri.template.UriTemplate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jenkins.plugins.codeinsights.api.dto.Report;
import java.io.IOException;

public class CodeInsightsPublisher implements AutoCloseable {
    private CodeInsightsContext context;
    private BitbucketApi bbClient;

    public CodeInsightsPublisher(final CodeInsightsContext context) {
        this.context = context;
        BitbucketSCMSource scmSource = context.resolveSource();
        if (scmSource != null) {
            bbClient = scmSource.buildBitbucketClient();
        }
    }

    /**
     * Publish the given Code Insights report to Bitbucket Cloud.
     *
     * @param report to publish
     * @throws IOException when occur error during publishing.
     */
    @SuppressFBWarnings("RV_ABSOLUTE_VALUE_OF_HASHCODE")
    public void publish(Report report) throws IOException {
        if (bbClient == null) {
            return;
        }

        String reportId = report.getReportType() + "-" + Math.abs(context.getJob().getUrl().hashCode());
        report.setExternalId(reportId);

        String url = UriTemplate.fromTemplate("/2.0/repositories/{workspace}/{repo_slug}/commit/{commit}/reports/{reportId}")
                .set("workspace", context.getOwner())
                .set("repo_slug", context.getRepository())
                .set("commit", context.getHeadSha())
                .set("reportId", report.getExternalId())
                .expand();

        BitbucketAuthenticatedClient authClient = bbClient.adapt(BitbucketAuthenticatedClient.class);
        authClient.put(url, report);
    }

    @Override
    public void close() throws IOException {
        if (bbClient != null) {
            bbClient.close();
        }
    }

}
