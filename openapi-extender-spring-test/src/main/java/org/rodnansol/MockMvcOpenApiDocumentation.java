package org.rodnansol;

/**
 * Static factory methods for the document reporter classes.
 */
public final class MockMvcOpenApiDocumentation {

    private MockMvcOpenApiDocumentation() {
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param operation   operation's name.
     * @param description description.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String operation, String description) {
        return new ApiResponseDocumentReporter(operation, description);
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param operation       operation's name.
     * @param description     description.
     * @param outputDirectory output directory for the generated resources.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String operation, String description, String outputDirectory) {
        return new ApiResponseDocumentReporter(operation, description, outputDirectory);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param operation   operation's name.
     * @param description description.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String operation, String description) {
        return new RequestBodyDocumentReporter(operation, description);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param operation       operation's name.
     * @param description     description.
     * @param outputDirectory output directory for the generated resources.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String operation, String description, String outputDirectory) {
        return new RequestBodyDocumentReporter(operation, description, outputDirectory);
    }

}
