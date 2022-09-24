package org.rodnansol.core.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenApiExamplePopulationAction {

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();

    public void populateExampleReferenceContext(OpenAPI openAPI) {
        for (Map.Entry<ExampleReferenceKey, List<ExampleReference>> referenceKeyListEntry : exampleReferenceContext.getReferences().entrySet()) {
            ExampleReferenceKey key = referenceKeyListEntry.getKey();
            referenceKeyListEntry.getValue().forEach(exampleReference -> {
                addExample(openAPI, key, exampleReference);
            });
        }
    }

    private void addExample(OpenAPI openAPI, ExampleReferenceKey referenceKey, ExampleReference exampleReference) {
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
