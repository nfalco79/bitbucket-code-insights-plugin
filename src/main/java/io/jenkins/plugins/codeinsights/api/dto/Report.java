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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Report {
    public static class Data {
        private ReportDataType type;
        private String title;
        private Object value;

        public ReportDataType getType() {
            return type;
        }

        public void setType(ReportDataType type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    private String type;
    @JsonProperty("external_id")
    private String externalId;
    private UUID uuid;
    private ReportResult result;
    private String link;
    @JsonProperty("remote_link_enabled")
    private boolean remoteLinkEnabled;
    @JsonProperty("logo_url")
    private URL logo;
    @JsonProperty("report_type")
    private ReportType reportType;
    private String title;
    private String details;
    private List<Data> data = new ArrayList<Report.Data>();
    @JsonProperty("created_on")
    @JsonFormat(shape = Shape.NUMBER, with = Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    private Date createdOn;
    @JsonProperty("updated_on")
    @JsonFormat(shape = Shape.NUMBER, with = Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    private Date updatedOn;
    @JsonIgnore
    private List<Annotation> annotations = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ReportResult getResult() {
        return result;
    }

    public void setResult(ReportResult result) {
        this.result = result;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isRemoteLinkEnabled() {
        return remoteLinkEnabled;
    }

    public void setRemoteLinkEnabled(boolean remoteLinkEnabled) {
        this.remoteLinkEnabled = remoteLinkEnabled;
    }

    public URL getLogo() {
        return logo;
    }

    public void setLogo(URL logo) {
        this.logo = logo;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
