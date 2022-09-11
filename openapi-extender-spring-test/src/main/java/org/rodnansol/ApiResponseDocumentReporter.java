package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * 0
 * Result handler implementation that documents the response of the incoming MvcResult.
 */
public class ApiResponseDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseDocumentReporter.class);
    private final ApiResponseExampleFileOutputResourceGenerator apiResponseExampleDocumenter;
    private final String operation;
    private final String description;

    public ApiResponseDocumentReporter(String operation, String description) {
        this(operation, description, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String operation, String description, String outputDirectory) {
        this.operation = operation;
        this.description = description;
        this.apiResponseExampleDocumenter = new ApiResponseExampleFileOutputResourceGenerator(outputDirectory);
    }

    @Override
    public void handle(MvcResult result) {
        try {
            MockHttpServletResponse response = result.getResponse();
            apiResponseExampleDocumenter.generateResources(new ApiResponseExampleFileOutputResourceGenerator.Params(operation, description, response.getStatus(), response.getContentType(), response.getContentAsByteArray()));
        } catch (Exception e) {
            throw new OpenApiExtenderResultHandlerException("Error during documenting response", e);
        }
    }

}
