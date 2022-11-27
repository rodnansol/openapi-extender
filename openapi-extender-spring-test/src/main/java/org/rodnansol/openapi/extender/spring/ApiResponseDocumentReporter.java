package org.rodnansol.openapi.extender.spring;

import org.rodnansol.openapi.extender.generator.ApiResponseExampleFileOutputResourceGenerator;
import org.rodnansol.openapi.extender.generator.ReportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * Result handler implementation that documents the response of the incoming MvcResult.
 */
public class ApiResponseDocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseDocumentReporter.class);
    private final ApiResponseExampleFileOutputResourceGenerator apiResponseExampleDocumenter;
    private final String operation;
    private final String name;
    private final String description;

    public ApiResponseDocumentReporter() {
        this(null, null, null, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String name) {
        this(null, name, null, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String operation, String name) {
        this(operation, name, null, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String operation, String name, String description) {
        this(operation, name, description, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseDocumentReporter(String operation, String name, String description, String outputDirectory) {
        this(operation, name, description, new ApiResponseExampleFileOutputResourceGenerator(outputDirectory));
    }

    ApiResponseDocumentReporter(String operation, String name, String description, ApiResponseExampleFileOutputResourceGenerator apiResponseExampleDocumenter) {
        this.operation = operation;
        this.name = name;
        this.description = description;
        this.apiResponseExampleDocumenter = apiResponseExampleDocumenter;
    }

    @Override
    public void handle(MvcResult result) {
        try {
            ReportParams params = createReportParamsByResult(result);
            apiResponseExampleDocumenter.generateResources(params);
        } catch (Exception e) {
            throw new OpenApiExtenderResultHandlerException("Error during documenting response", e);
        }
    }

    private ReportParams createReportParamsByResult(MvcResult result) {
        String finalOperation = MvcResultReader.getOperationName(operation, result);
        MockHttpServletResponse response = result.getResponse();
        String finalName = getFinalName(finalOperation, response);
        ReportParams params = new ReportParams(finalOperation, finalName, response.getStatus(), response.getContentType(), response.getContentAsByteArray());
        params.setDescription(description);
        return params;
    }

    private String getFinalName(String finalOperation, MockHttpServletResponse response) {
        if (name != null) {
            return name;
        }
        return finalOperation + "-" + response.getStatus();
    }

}
