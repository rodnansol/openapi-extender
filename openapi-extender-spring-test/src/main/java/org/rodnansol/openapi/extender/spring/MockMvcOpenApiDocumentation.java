package org.rodnansol.openapi.extender.spring;

/**
 * Static factory methods for the document reporter classes.
 */
public final class MockMvcOpenApiDocumentation {

    private MockMvcOpenApiDocumentation() {
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param name name/key of the example.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse() {
        return new ApiResponseDocumentReporter();
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param name name/key of the example.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String name) {
        return new ApiResponseDocumentReporter(name);
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param operation operation's name.
     * @param name      name/key of the example.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String operation, String name) {
        return new ApiResponseDocumentReporter(operation, name);
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param operation   operation's name.
     * @param name        name/key of the example.
     * @param description optional description.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String operation, String name, String description) {
        return new ApiResponseDocumentReporter(operation, name, description);
    }

    /**
     * Creates an {@link ApiResponseDocumentReporter} with the incoming operation and description.
     *
     * @param operation       operation's name.
     * @param name            name/key of the example.
     * @param description     optional description.
     * @param outputDirectory output directory for the generated resources.
     * @return {@link ApiResponseDocumentReporter} instance.
     */
    public static ApiResponseDocumentReporter documentResponse(String operation, String name, String description, String outputDirectory) {
        return new ApiResponseDocumentReporter(operation, name, description, outputDirectory);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest() {
        return new RequestBodyDocumentReporter();
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param name name/key of the example.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String name) {
        return new RequestBodyDocumentReporter(name);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param operation operation's name.
     * @param name      name/key of the example.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String operation, String name) {
        return new RequestBodyDocumentReporter(operation, name);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param operation   operation's name.
     * @param name        name/key of the example.
     * @param description optional description.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String operation, String name, String description) {
        return new RequestBodyDocumentReporter(operation, name, description);
    }

    /**
     * Creates an {@link RequestBodyDocumentReporter} with the incoming operation and description.
     *
     * @param operation       operation's name.
     * @param name            name/key of the example.
     * @param description     optional description.
     * @param outputDirectory output directory for the generated resources.
     * @return {@link RequestBodyDocumentReporter} instance.
     */
    public static RequestBodyDocumentReporter documentRequest(String operation, String name, String description, String outputDirectory) {
        return new RequestBodyDocumentReporter(operation, name, description, outputDirectory);
    }

}
