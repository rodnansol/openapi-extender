package org.rodnansol.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.rodnansol.generator.MediaTypeUtil.MEDIA_TYPES;

public class ApiResponseExampleFileOutputResourceGenerator extends AbstractFileOutputResourceGenerator {

    private static final String OPENAPI_EXTENDER_RESPONSES_FOLDER = "openapi-extender/responses/";

    public static final String DEFAULT_OUTPUT_DIRECTORY = "target/classes/" + OPENAPI_EXTENDER_RESPONSES_FOLDER;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseExampleFileOutputResourceGenerator.class);

    private final String outputDirectory;

    public ApiResponseExampleFileOutputResourceGenerator() {
        this(DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseExampleFileOutputResourceGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    protected String createFilePath(String operation, int status, String contentType, String name, String optionalDescription) {
        if (optionalDescription != null) {
            return String.format("%s%s_%d_%s__%s%s", outputDirectory, operation, status, name, optionalDescription, MEDIA_TYPES.get(contentType));
        }
        return String.format("%s%s_%d_%s%s", outputDirectory, operation, status, name, MEDIA_TYPES.get(contentType));
    }

    protected String getFolderName(String operation) {
        return outputDirectory + "/";
    }

}
