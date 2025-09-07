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
package io.jenkins.plugins.codeinsights.impl;

import hudson.Util;
import hudson.model.Run;
import hudson.tasks.junit.TestResult;
import hudson.tasks.junit.TestResultAction;
import io.jenkins.plugins.codeinsights.CodeInsightsContext;
import io.jenkins.plugins.codeinsights.api.CodeInsightsReporBuilder;
import io.jenkins.plugins.codeinsights.api.dto.Report;
import io.jenkins.plugins.codeinsights.api.dto.Report.Data;
import io.jenkins.plugins.codeinsights.api.dto.ReportDataType;
import io.jenkins.plugins.codeinsights.api.dto.ReportResult;
import io.jenkins.plugins.codeinsights.api.dto.ReportType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.jenkinsci.plugins.variant.OptionalExtension;

@OptionalExtension(requirePlugins = "junit")
public class CodeInsightsTestReportBuilder implements CodeInsightsReporBuilder {

    @Override
    public Optional<Report> build(Run<?, ?> run, CodeInsightsContext context) {
        TestResultAction action = run.getAction(TestResultAction.class);
        if (action != null) {
            TestResult testResult = action.getResult();
            Report testReport = new Report();
            testReport.setTitle(testResult.getTitle());
            testReport.setLink(Util.ensureEndsWith(context.getRootURL(), "/") + testResult.getUrl());
            testReport.setRemoteLinkEnabled(true);
            testReport.setReportType(ReportType.TEST);
            testReport.setType("report");
            testReport.getData().addAll(buildData(testResult));
            if (testResult.getFailCount() > 0) {
                testReport.setDetails("There are failed tests");
            } else {
                testReport.setDetails("Reports no tests failure");
            }
            if (testResult.getFailCount() > 0) {
                testReport.setResult(ReportResult.FAILED);
            } else {
                testReport.setResult(ReportResult.PASSED);
            }
            return Optional.of(testReport);
        } else {
            return Optional.empty();
        }
    }

    private Collection<Data> buildData(TestResult result) {
        List<Data> datas = new ArrayList<>();
        if (result.getTotalCount() != 0) {
            Data data = new Data();
            data.setTitle("Number of test cases");
            data.setType(ReportDataType.NUMBER);
            data.setValue(result.getTotalCount());
            datas.add(data);
        }
        if (result.getSkipCount() != 0) {
            Data data = new Data();
            data.setTitle("Skipped Tests");
            data.setType(ReportDataType.NUMBER);
            data.setValue(result.getSkipCount());
            datas.add(data);
        }
        if (result.getFailCount() != 0) {
            Data data = new Data();
            data.setTitle("Failed Tests");
            data.setType(ReportDataType.NUMBER);
            data.setValue(result.getFailCount());
            datas.add(data);
        }
        if (result.getPassCount() != 0) {
            Data data = new Data();
            data.setTitle("Passed Tests");
            data.setType(ReportDataType.NUMBER);
            data.setValue(result.getPassCount());
            datas.add(data);
        }
        if (result.getDuration() > 0) {
            Data data = new Data();
            data.setTitle("Test Duration");
            data.setType(ReportDataType.DURATION);
            data.setValue((long) result.getDuration() * 1000l);
            datas.add(data);
        }
        return datas;
    }
}
