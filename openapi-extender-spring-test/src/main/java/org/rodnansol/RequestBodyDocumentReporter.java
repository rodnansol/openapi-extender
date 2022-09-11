package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * Result handler implementation that documents the request of the incoming MvcResult.
 */
public class RequestBodyDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyDocumentReporter.class);
    private final RequestBodyExampleFileOutputResourceGenerator requestBodyExampleDocumenter;
    private final String operation;
    private final String description;

    public RequestBodyDocumentReporter(String operation, String description) {
        this(operation, description, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyDocumentReporter(String operation, String description, String outputDirectory) {
        this.operation = operation;
        this.description = description;
        this.requestBodyExampleDocumenter = new RequestBodyExampleFileOutputResourceGenerator(outputDirectory);
    }

    @Override
    public void handle(MvcResult result) {
        try {
            MockHttpServletResponse response = result.getResponse();
            MockHttpServletRequest request = result.getRequest();
            requestBodyExampleDocumenter.generateResources(new RequestBodyExampleFileOutputResourceGenerator.Params(operation, description, response.getStatus(), request.getContentType(), request.getContentAsByteArray()));
        } catch (Exception e) {
            throw new OpenApiExtenderResultHandlerException("Error during documenting request body", e);
        }
    }

}
