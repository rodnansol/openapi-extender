package org.rodnansol.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.rodnansol.core.example.ExampleReferenceContext;
import org.rodnansol.core.openapi.OpenApiExamplePopulationAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;

/**
 *
 */
public class OpenApiExampleExtenderCustomizer implements OpenApiCustomiser {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiExampleExtenderCustomizer.class);

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();
    private final OpenApiExamplePopulationAction openApiExamplePopulationAction;

    public OpenApiExampleExtenderCustomizer() {
        this(new OpenApiExamplePopulationAction());
    }

    public OpenApiExampleExtenderCustomizer(OpenApiExamplePopulationAction openApiExamplePopulationAction) {
        this.openApiExamplePopulationAction = openApiExamplePopulationAction;
    }

    @Override
    public void customise(OpenAPI openApi) {
        LOGGER.debug("Populating given OpenAPI object instance with examples from the context.");
        processResources(openApi);
        exampleReferenceContext.clearContext();
    }

    private void processResources(OpenAPI openApi) {
        openApiExamplePopulationAction.populateOpenApiExamplesFromContext(openApi);
    }
}
