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
package io.jenkins.plugins.codeinsights.api.dto;

// https://developer.atlassian.com/cloud/bitbucket/rest/api-group-reports/#api-repositories-workspace-repo-slug-commit-commit-reports-reportid-put
public enum ReportDataType {
    /*
     * The value will be read as a JSON boolean and displayed as 'Yes' or 'No'.
     */
    BOOLEAN,
    /*
     * The value will be read as a JSON number in the form of a Unix timestamp
     * (milliseconds) and will be displayed as a relative date if the date is
     * less than one week ago, otherwise it will be displayed as an absolute
     * date.
     */
    DATE,
    /*
     * The value will be read as a JSON number in milliseconds and will be
     * displayed in a human readable duration format.
     */
    DURATION,
    /*
     * The value will be read as a JSON object containing the fields "text" and
     * "href" and will be displayed as a clickable link on the report. Example:
     * {"text": "Link text here", "href": "https://link.to.annotation/in/external/tool"}
     */
    LINK,
    /*
     * The value will be read as a JSON number and large numbers will be
     * displayed in a human readable format (e.g. 14.3k).
     */
    NUMBER,
    /*
     * The value will be read as a JSON number between 0 and 100 and will be
     * displayed with a percentage sign.
     */
    PERCENTAGE,
    /* The value will be read as a JSON string and will be displayed as-is. */
    TEXT
}
