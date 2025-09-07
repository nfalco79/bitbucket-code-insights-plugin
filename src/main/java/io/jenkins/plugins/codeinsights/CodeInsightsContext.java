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
import com.cloudbees.jenkins.plugins.bitbucket.api.endpoint.BitbucketEndpoint;
import com.cloudbees.jenkins.plugins.bitbucket.api.endpoint.BitbucketEndpointProvider;
import com.cloudbees.jenkins.plugins.bitbucket.api.endpoint.EndpointType;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.util.Optional;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMRevision;
import org.apache.commons.lang3.StringUtils;
import org.jenkinsci.plugins.displayurlapi.DisplayURLProvider;

public class CodeInsightsContext {

    static CodeInsightsContext fromRun(final Run<?, ?> run, final DisplayURLProvider urlProvider, final SCMFacade scmFacade) {
        return new CodeInsightsContext(run.getParent(), run, urlProvider, scmFacade);
    }

    static CodeInsightsContext fromJob(final Job<?, ?> job, final DisplayURLProvider urlProvider, final SCMFacade scmFacade) {
        return new CodeInsightsContext(job, null, urlProvider, scmFacade);
    }

    private final Job<?, ?> job;
    @CheckForNull
    private final Run<?, ?> run;
    private final DisplayURLProvider urlProvider;
    private final SCMFacade scmFacade;
    @CheckForNull
    private final String sha;

    /**
     * Creates a {@link CodeInsightsContext} according to the job and run, if provided. All attributes are computed during this period.
     *
     * @param job
     *         a Multibranch Source project
     * @param run
     *         a run of a Bitbucket Branch Source project
     * @param runURL
     *         the URL to the Jenkins run
     * @param scmFacade
     *         a facade for Jenkins SCM
     */
    private CodeInsightsContext(final Job<?, ?> job, @CheckForNull final Run<?, ?> run, final DisplayURLProvider urlProvider, final SCMFacade scmFacade) {
        this.job = job;
        this.urlProvider = urlProvider;
        this.scmFacade = scmFacade;
        this.run = run;
        this.sha = Optional.ofNullable(run).map(this::resolveHeadSha).orElse(resolveHeadSha(job));
    }

    /**
     * Returns the URL of the run's summary page, e.g. https://ci.jenkins.io/job/Core/job/jenkins/job/master/2000/.
     *
     * @return the URL of the summary page
     */
    public String getRunURL() {
        if (run != null) {
            return urlProvider.getRunURL(run);
        } else {
            return null;
        }
    }

    /**
     * Returns the configured URL of Jenkins, e.g. https://ci.jenkins.io/jenkins.
     *
     * @return the root URL of this Jenkins instance
     */
    public String getRootURL() {
        return urlProvider.getRoot();
    }

    Job<?, ?> getJob() {
        return job;
    }

    public final SCMFacade getSCMFacade() {
        return scmFacade;
    }

    /**
     * Returns the commit sha of the run.
     *
     * @return the commit sha of the run
     */
    public String getHeadSha() {
        if (StringUtils.isBlank(sha)) {
            throw new IllegalStateException("No SHA found for job: " + getJob().getName());
        }

        return sha;
    }

    /**
     * Returns the source repository's full name of the run. The full name consists of the owner's name and the
     * repository's name, e.g. jenkins-ci/jenkins
     *
     * @return the source repository's full name
     */
    public String getRepository() {
        BitbucketSCMSource source = resolveSource();
        if (source == null) {
            throw new IllegalStateException("No GitHub SCM source found for job: " + getJob().getName());
        }
        else {
            return source.getRepository();
        }
    }

    public String getOwner() {
        return Optional.ofNullable(resolveSource()).map(BitbucketSCMSource::getRepoOwner).orElse(null);
    }

    /**
     * Returns whether the context is valid (with all properties functional) to use.
     *
     * @param logger
     *         the filtered logger
     * @return whether the context is valid to use
     */
    public boolean isValid(final TaskListener logger) {
        logger.getLogger().println("Trying to resolve checks parameters from Bitbucket SCM...");

        BitbucketSCMSource source = resolveSource();
        if (source == null) {
            logger.error("Job does not use Bitbucket SCM");

            return false;
        }

        if (StringUtils.isBlank(sha)) {
            logger.error("No HEAD SHA found for %s", getRepository());

            return false;
        }

        EndpointType endpointType = BitbucketEndpointProvider.lookupEndpoint(source.getServerUrl())
                .map(BitbucketEndpoint::getType)
                .orElse(null);
        if (endpointType != EndpointType.CLOUD) {
            logger.error("Bitbucket Data Center does is not supports code insights");

            return false;
        }

        return true;
    }

    /**
     * Returns the URL of the run's summary page, e.g. https://ci.jenkins.io/job/Core/job/jenkins/job/master/2000/.
     *
     * @return the URL of the summary page
     */
    protected Optional<Run<?, ?>> getRun() {
        return Optional.ofNullable(run);
    }

    @CheckForNull
    BitbucketSCMSource resolveSource() {
        return scmFacade.findBitbucketSCMSource(getJob()).orElse(null);
    }

    @CheckForNull
    private String resolveHeadSha(final Run<?, ?> theRun) {
        BitbucketSCMSource source = resolveSource();
        if (source != null) {
            Optional<SCMRevision> revision = scmFacade.findRevision(source, theRun);
            if (revision.isPresent()) {
                return scmFacade.findHash(revision.get()).orElse(null);
            }
        }

        return null;
    }

    @CheckForNull
    private String resolveHeadSha(final Job<?, ?> job) {
        BitbucketSCMSource source = resolveSource();
        Optional<SCMHead> head = scmFacade.findHead(job);
        if (source != null && head.isPresent()) {
            Optional<SCMRevision> revision = scmFacade.findRevision(source, head.get());
            if (revision.isPresent()) {
                return scmFacade.findHash(revision.get()).orElse(null);
            }
        }

        return null;
    }

}
