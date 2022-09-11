package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.rodnansol.MediaTypeUtil.MEDIA_TYPES;

public class RequestBodyExampleFileOutputResourceGenerator extends AbstractFileOutputResourceGenerator {

    private static final String OPENAPI_EXTENDER_REQUEST_FOLDER = "openapi-extender/requests/";
    static final String DEFAULT_OUTPUT_DIRECTORY = "target/classes/" + OPENAPI_EXTENDER_REQUEST_FOLDER;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyExampleFileOutputResourceGenerator.class);
    private final String outputDirectory;

    public RequestBodyExampleFileOutputResourceGenerator() {
        this(DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyExampleFileOutputResourceGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    protected String createAggregatorFile(String operation, String description, int status, String contentType) {
        return getFolderName(operation) + status + "_" + description + MEDIA_TYPES.get(contentType);
    }

    protected String getFolderName(String operation) {
        return outputDirectory + operation + "/";
    }

    static class Params implements ResourceGeneratorParams {

        private final String operation;
        private final String description;
        private final int status;
        private final String contentType;
        private final byte[] content;

        public Params(String operation, String description, int status, String contentType, byte[] content) {
            this.operation = operation;
            this.description = description;
            this.status = status;
            this.contentType = contentType;
            this.content = content;
        }

        public String getOperation() {
            return operation;
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

}
