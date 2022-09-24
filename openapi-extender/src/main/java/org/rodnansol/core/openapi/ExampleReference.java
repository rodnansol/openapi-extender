package org.rodnansol.core.openapi;

/**
 * Represents an example reference read from a resource.
 */
public class ExampleReference {

    private final String operationId;
    private final String statusCode;
    private final String mediaType;
    private final String name;
    private final ExampleReferenceType type;
    private String content;
    private String description;

    public ExampleReference(String operationId, String statusCode, String mediaType, String name, ExampleReferenceType type) {
        this.operationId = operationId;
        this.statusCode = statusCode;
        this.mediaType = mediaType;
        this.name = name;
        this.type = type;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getName() {
        return name;
    }

    public ExampleReferenceType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the referred name
     *
     * @return referred name.
     */
    public String getReferredName() {
        return String.format("%s_%s_%s_%s", type.name().toLowerCase(), operationId, mediaType.replace("/", "_"), name);
    }

    //###################################
    //#############GENERATED#############
    //###################################

    private ExampleReference(Builder builder) {
        operationId = builder.operationId;
        statusCode = builder.statusCode;
        mediaType = builder.mediaType;
        name = builder.name;
        type = builder.type;
        content = builder.content;
        description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String operationId;
        private String statusCode;
        private String mediaType;
        private String name;
        private ExampleReferenceType type;
        private String content;
        private String description;

        private Builder() {
        }

        public Builder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(ExampleReferenceType type) {
            this.type = type;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ExampleReference build() {
            return new ExampleReference(this);
        }
    }
}
