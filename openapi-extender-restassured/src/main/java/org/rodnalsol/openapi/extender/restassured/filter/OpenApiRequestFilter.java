package org.rodnalsol.openapi.extender.restassured.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.rodnalsol.openapi.extender.restassured.exception.OpenApiFilterException;
import org.rodnansol.openapi.extender.generator.ReportParams;
import org.rodnansol.openapi.extender.generator.RequestBodyExampleFileOutputResourceGenerator;

/**
 * Rest assured filter class for generating OpenAPI request example.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class OpenApiRequestFilter implements Filter {

    /**
     * The OpenAPI request example generator.
     */
    private final RequestBodyExampleFileOutputResourceGenerator requestBodyExampleFileOutputResourceGenerator;

    /**
     * The operation's name.
     */
    private final String operation;

    /**
     * The name of the OpenAPI request example.
     */
    private final String name;

    /**
     * The description of the OpenAPI request example.
     */
    private final String description;

    /**
     * Creates a new {@link OpenApiRequestFilter} with the specified operation and name.
     * The description will be null and the output directory will be {@link RequestBodyExampleFileOutputResourceGenerator#DEFAULT_OUTPUT_DIRECTORY}.
     *
     * @param operation the operation's name.
     * @param name      the name of the OpenAPI request example.
     */
    public OpenApiRequestFilter(String operation, String name) {
        this(operation, name, null, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    /**
     * Creates a new {@link OpenApiRequestFilter} with the specified operation, name and description.
     * The output directory will be {@link RequestBodyExampleFileOutputResourceGenerator#DEFAULT_OUTPUT_DIRECTORY}.
     *
     * @param operation   the operation's name.
     * @param name        the name of the OpenAPI request example.
     * @param description the description of the OpenAPI request example.
     */
    public OpenApiRequestFilter(String operation, String name, String description) {
        this(operation, name, description, RequestBodyExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    /**
     * Creates a new {@link OpenApiRequestFilter} with the specified operation, name, description and output directory.
     *
     * @param operation       the operation's name.
     * @param name            the name of the OpenAPI request example.
     * @param description     the description of the OpenAPI request example.
     * @param outputDirectory the output directory of the OpenAPI request example.
     */
    public OpenApiRequestFilter(String operation, String name, String description, String outputDirectory) {
        this.operation = operation;
        this.name = name;
        this.description = description;
        this.requestBodyExampleFileOutputResourceGenerator = new RequestBodyExampleFileOutputResourceGenerator(outputDirectory);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        try {
            Response response = ctx.next(requestSpec, responseSpec);
            generateResources(requestSpec, response);
            return response;
        } catch (Exception e) {
            throw new OpenApiFilterException("Error during documenting response", e);
        }
    }

    private void generateResources(FilterableRequestSpecification requestSpec, Response response) throws IOException {
        byte[] requestBodyAsByteArray = requestSpec.getBody().toString().getBytes(StandardCharsets.UTF_8);
        ReportParams params = new ReportParams(operation, name, response.getStatusCode(), requestSpec.getContentType(), requestBodyAsByteArray);
        params.setDescription(description);
        requestBodyExampleFileOutputResourceGenerator.generateResources(params);
    }
}
