package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 *
 */
public class ApiResponseDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseDocumentReporter.class);
    private final ApiResponseExampleDocumenter apiResponseExampleDocumenter;
    private final String operation;
    private final String description;

    public ApiResponseDocumentReporter(String operation, String description) {
        this(operation, description, ApiResponseExampleDocumenter.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String operation, String description, String outputDirectory) {
        this.operation = operation;
        this.description = description;
        this.apiResponseExampleDocumenter = new ApiResponseExampleDocumenter(outputDirectory);
    }

    @Override
    public void handle(MvcResult result) {
        try {
            MockHttpServletResponse response = result.getResponse();
            apiResponseExampleDocumenter.document(new ApiResponseExampleDocumenter.Params(operation, description, response.getStatus(), response.getContentType(), response.getContentAsByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
