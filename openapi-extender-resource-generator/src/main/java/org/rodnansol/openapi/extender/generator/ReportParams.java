package org.rodnansol.openapi.extender.generator;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class to be used as the report parameter.
 */
public class ReportParams implements ResourceGeneratorParams {

    private final String operation;
    private final String name;
    private final int status;
    private final String contentType;
    private final byte[] content;
    private String description;

    public ReportParams(String operation, String name, int status, String contentType, byte[] content) {
        this.operation = operation;
        this.name = name;
        this.status = status;
        this.contentType = contentType;
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportParams that = (ReportParams) o;
        return status == that.status && Objects.equals(operation, that.operation) && Objects.equals(name, that.name) && Objects.equals(contentType, that.contentType) && Arrays.equals(content, that.content) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(operation, name, status, contentType, description);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
