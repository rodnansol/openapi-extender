package org.rodnansol.openapi.extender.springdoc.description;

/**
 * Class describing an operation configuration.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class OperationDescriptionConfiguration {

    private final String resourcesBasePath;
    private final String extension;

    public OperationDescriptionConfiguration(String resourcesBasePath, String extension) {
        this.resourcesBasePath = resourcesBasePath;
        this.extension = extension;
    }

    private OperationDescriptionConfiguration(Builder builder) {
        resourcesBasePath = builder.resourcesBasePath;
        extension = builder.extension;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getResourcesBasePath() {
        return resourcesBasePath;
    }

    public String getExtension() {
        return extension;
    }


    public static final class Builder {

        private String resourcesBasePath;
        private String extension = ".md";

        private Builder() {
        }

        public Builder resourcesBasePath(String resourcesBasePath) {
            this.resourcesBasePath = resourcesBasePath;
            return this;
        }

        public Builder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public OperationDescriptionConfiguration build() {
            return new OperationDescriptionConfiguration(this);
        }
    }
}
