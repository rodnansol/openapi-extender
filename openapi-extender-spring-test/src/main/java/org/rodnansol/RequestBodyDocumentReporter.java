package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 *
 */
public class RequestBodyDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyDocumentReporter.class);
    private final RequestBodyExampleDocumenter requestBodyExampleDocumenter;
    private final String operation;
    private final String description;

    public RequestBodyDocumentReporter(String operation, String description) {
        this(operation, description, RequestBodyExampleDocumenter.DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyDocumentReporter(String operation, String description, String outputDirectory) {
        this.operation = operation;
        this.description = description;
        this.requestBodyExampleDocumenter = new RequestBodyExampleDocumenter(outputDirectory);
    }

    @Override
    public void handle(MvcResult result) {
        try {
            MockHttpServletResponse response = result.getResponse();
            MockHttpServletRequest request = result.getRequest();
            requestBodyExampleDocumenter.document(new RequestBodyExampleDocumenter.Params(operation, description, response.getStatus(), request.getContentType(), request.getContentAsByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
