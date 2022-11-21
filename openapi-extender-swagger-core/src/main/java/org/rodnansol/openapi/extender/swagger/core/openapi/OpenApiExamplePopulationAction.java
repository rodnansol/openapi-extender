package org.rodnansol.openapi.extender.swagger.core.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReference;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action that reads the example reference context and populates the OpenAPI documentation holder object with all reference entries.
 */
public class OpenApiExamplePopulationAction {

    private final ExampleReferenceContext exampleReferenceContext;

    public OpenApiExamplePopulationAction() {
        this(ExampleReferenceContext.getInstance());
    }

    public OpenApiExamplePopulationAction(ExampleReferenceContext exampleReferenceContext) {
        this.exampleReferenceContext = exampleReferenceContext;
    }

    /**
     * Populates the incoming OpenAPI instance with all available example references.
     *
     * @param openAPI documentation instance.
     */
    public void populateOpenApiExamplesFromContext(OpenAPI openAPI) {
        for (List<ExampleReference> references : exampleReferenceContext.getReferences().values()) {
            references.forEach(exampleReference -> addExample(openAPI, exampleReference));
        }
    }

    private void addExample(OpenAPI openAPI, ExampleReference exampleReference) {
        Components components = getComponents(openAPI);
        Map<String, Example> examples = getExamples(components);
        if (!examples.containsKey(exampleReference.getName())) {
            Example examplesItem = new Example();
            examplesItem.setValue(exampleReference.getContent());
            examplesItem.setDescription(exampleReference.getDescription());
            components.addExamples(exampleReference.getReferredName(), examplesItem);
        }
    }

    private Map<String, Example> getExamples(Components components) {
        Map<String, Example> examples = components.getExamples();
        if (examples == null) {
            examples = new HashMap<>();
            components.setExamples(examples);
        }
        return examples;
    }

    private Components getComponents(OpenAPI openAPI) {
        Components components = openAPI.getComponents();
        if (components == null) {
            components = new Components();
            openAPI.setComponents(components);
        }
        return components;
    }
}
