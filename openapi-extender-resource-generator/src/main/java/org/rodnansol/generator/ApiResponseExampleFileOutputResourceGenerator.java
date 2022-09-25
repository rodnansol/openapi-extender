package org.rodnansol.generator;

import static org.rodnansol.generator.MediaTypeUtil.MEDIA_TYPES;

/**
 * Response example file writer class.
 */
public class ApiResponseExampleFileOutputResourceGenerator extends AbstractFileOutputResourceGenerator {

    private static final String OPENAPI_EXTENDER_RESPONSES_FOLDER = "openapi-extender/responses/";

    public static final String DEFAULT_OUTPUT_DIRECTORY = "target/classes/" + OPENAPI_EXTENDER_RESPONSES_FOLDER;

    public ApiResponseExampleFileOutputResourceGenerator() {
        this(DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseExampleFileOutputResourceGenerator(String outputDirectory) {
        super(outputDirectory);
    }

    protected String createFilePath(String operation, int status, String contentType, String name, String optionalDescription) {
        if (optionalDescription != null) {
            return String.format("%s%s_%d_%s__%s%s", outputDirectory, operation, status, name, optionalDescription, MEDIA_TYPES.get(contentType));
        }
        return String.format("%s%s_%d_%s%s", outputDirectory, operation, status, name, MEDIA_TYPES.get(contentType));
    }

}
