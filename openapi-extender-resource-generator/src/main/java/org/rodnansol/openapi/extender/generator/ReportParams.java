package org.rodnansol.openapi.extender.generator;

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

}
