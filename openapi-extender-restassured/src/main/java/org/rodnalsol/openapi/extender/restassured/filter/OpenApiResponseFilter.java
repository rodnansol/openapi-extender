package org.rodnalsol.openapi.extender.restassured.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.io.IOException;

import org.rodnalsol.openapi.extender.restassured.exception.OpenApiFilterException;
import org.rodnansol.generator.ApiResponseExampleFileOutputResourceGenerator;
import org.rodnansol.generator.ReportParams;

/**
 * Rest assured filter class for generating OpenAPI response example.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class OpenApiResponseFilter implements Filter {

    /**
     * The OpenAPI response example generator.
     */
    private final ApiResponseExampleFileOutputResourceGenerator apiResponseExampleFileOutputResourceGenerator;

    /**
     * The operation's name.
     */
    private final String operation;

    /**
     * The name of the OpenAPI response example.
     */
    private final String name;

    /**
     * The description of the OpenAPI response example.
     */
    private final String description;

    /**
     * Creates a new {@link OpenApiResponseFilter} with the specified operation and name.
     * The description will be null and the output directory will be {@link ApiResponseExampleFileOutputResourceGenerator#DEFAULT_OUTPUT_DIRECTORY}.
     *
     * @param operation the operation's name.
     * @param name      the name of the OpenAPI response example.
     */
    public OpenApiResponseFilter(String operation, String name) {
        this(operation, name, null, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    /**
     * Creates a new {@link OpenApiResponseFilter} with the specified operation, name and description.
     * The output directory will be {@link ApiResponseExampleFileOutputResourceGenerator#DEFAULT_OUTPUT_DIRECTORY}.
     *
     * @param operation   the operation's name.
     * @param name        the name of the OpenAPI response example.
     * @param description the description of the OpenAPI response example.
     */
    public OpenApiResponseFilter(String operation, String name, String description) {
        this(operation, name, description, ApiResponseExampleFileOutputResourceGenerator.DEFAULT_OUTPUT_DIRECTORY);
    }

    /**
     * Creates a new {@link OpenApiResponseFilter} with the specified operation, name, description and output directory.
     *
     * @param operation       the operation's name.
     * @param name            the name of the OpenAPI response example.
     * @param description     the description of the OpenAPI response example.
     * @param outputDirectory the output directory of the OpenAPI response example.
     */
    public OpenApiResponseFilter(String operation, String name, String description, String outputDirectory) {
        this.operation = operation;
        this.name = name;
        this.description = description;
        this.apiResponseExampleFileOutputResourceGenerator = new ApiResponseExampleFileOutputResourceGenerator(outputDirectory);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        try {
            Response response = ctx.next(requestSpec, responseSpec);
            generateResources(response);
            return response;
        } catch (Exception e) {
            throw new OpenApiFilterException("Error during documenting response", e);
        }
    }

    private void generateResources(Response response) throws IOException {
        ReportParams params = new ReportParams(operation, name, response.getStatusCode(), response.getContentType(), response.asByteArray());
        params.setDescription(description);
        apiResponseExampleFileOutputResourceGenerator.generateResources(params);
    }
}
