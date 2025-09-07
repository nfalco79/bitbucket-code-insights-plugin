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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

public class Annotation {
    private String type;
    @JsonProperty("external_id")
    private String externalId;
    private UUID uuid;
    @JsonProperty("annotation_type")
    private AnnotationType annotationType;
    private String path;
    private int line;
    private String title;
    private String summary;
    private String details;
    private AnnotationResult result;
    private AnnotationSeverity severity;
    private String link;
    @JsonProperty("created_on")
    @JsonFormat(shape = Shape.NUMBER, with = Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    private Date createdOn;
    @JsonProperty("updated_on")
    @JsonFormat(shape = Shape.NUMBER, with = Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    private Date updatedOn;

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

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public AnnotationResult getResult() {
        return result;
    }

    public void setResult(AnnotationResult result) {
        this.result = result;
    }

    public AnnotationSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AnnotationSeverity severity) {
        this.severity = severity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
