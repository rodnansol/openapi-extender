package org.rodnansol.openapi.extender.spring;

import io.swagger.v3.oas.annotations.Operation;
import org.rodnansol.openapi.extender.generator.ReportParams;
import org.rodnansol.openapi.extender.generator.RequestBodyExampleFileOutputResourceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.method.HandlerMethod;

/**
 * Result handler implementation that documents the request of the incoming MvcResult.
 */
public class RequestBodyDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyDocumentReporter.class);
    private final RequestBodyExampleFileOutputResourceGenerator requestBodyExampleDocumenter;
    private final String operation;
    private final String name;
    private final String description;

    public RequestBodyDocumentReporter() {
        this(null, null, null, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyDocumentReporter(String name) {
        this(null, name, null, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyDocumentReporter(String operation, String name) {
        this(operation, name, null, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public RequestBodyDocumentReporter(String operation, String name, String description) {
        this(operation, name, description, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }


    public RequestBodyDocumentReporter(String operation, String name, String description, String outputDirectory) {
        this.operation = operation;
        this.name = name;
        this.description = description;
        this.requestBodyExampleDocumenter = new RequestBodyExampleFileOutputResourceGenerator(outputDirectory);
    }

    @Override
    public void handle(MvcResult result) {
        try {
            MockHttpServletResponse response = result.getResponse();
            MockHttpServletRequest request = result.getRequest();
            String finalOperation = getOperationName(result);
            String finalName = getFinalName(finalOperation, response);
            ReportParams params = new ReportParams(finalOperation, finalName, response.getStatus(), request.getContentType(), request.getContentAsByteArray());
            params.setDescription(description);
            requestBodyExampleDocumenter.generateResources(params);
        } catch (Exception e) {
            throw new OpenApiExtenderResultHandlerException("Error during documenting request body", e);
        }
    }

    private String getFinalName(String finalOperation, MockHttpServletResponse response) {
        if (name != null) {
            return name;
        }
        return finalOperation + "-" + response.getStatus();
    }

    private String getOperationName(MvcResult result) {
        String finalOperation = null;
        if (operation != null) {
            finalOperation = operation;
        } else {
            if (result.getHandler() instanceof HandlerMethod) {
                HandlerMethod handler = (HandlerMethod) result.getHandler();
                if (handler != null && handler.getMethod() != null) {
                    Operation operationAnnotation = handler.getMethod().getAnnotation(Operation.class);
                    finalOperation = operationAnnotation != null && operationAnnotation.operationId() != null ? operationAnnotation.operationId() : handler.getMethod().getName();
                }
            }
        }
        return finalOperation;
    }

}
