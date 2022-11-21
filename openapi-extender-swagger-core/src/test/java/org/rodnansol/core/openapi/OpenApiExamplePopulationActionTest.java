package org.rodnansol.core.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReference;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceContext;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceKey;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceType;
import org.rodnansol.openapi.extender.swagger.core.openapi.OpenApiExamplePopulationAction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenApiExamplePopulationActionTest {

    private static final String CONTENT = "Hello World";
    private static final String DESCRIPTION = "Hello World Description";
    @Mock
    ExampleReferenceContext exampleReferenceContext;

    @InjectMocks
    OpenApiExamplePopulationAction underTest;

    @Test
    void populateOpenApiExamplesFromContext_ShouldNotTouchOpenApi_WhenReferenceContextIsEmpty() {
        // Given
        OpenAPI openAPI = mock(OpenAPI.class);
        when(exampleReferenceContext.getReferences()).thenReturn(new HashMap<>());

        // When
        underTest.populateOpenApiExamplesFromContext(openAPI);

        // Then
        verifyNoInteractions(openAPI);
    }

    @Test
    void populateOpenApiExamplesFromContext_ShouldPopulateOpenApiExamples_WhenReferenceContextHasEntries() {
        // Given
        OpenAPI openAPI = new OpenAPI();
        Map<ExampleReferenceKey, List<ExampleReference>> referenceMap = new HashMap<>();
        List<ExampleReference> references = new ArrayList<>();
        ExampleReference exampleReference = new ExampleReference("testOperation", "200", "application/json", "test", ExampleReferenceType.REQUEST);
        exampleReference.setContent(CONTENT);
        exampleReference.setDescription(DESCRIPTION);
        references.add(exampleReference);
        referenceMap.put(new ExampleReferenceKey("testOperation", ExampleReferenceType.REQUEST), references);
        when(exampleReferenceContext.getReferences()).thenReturn(referenceMap);

        // When
        underTest.populateOpenApiExamplesFromContext(openAPI);

        // Then
        Example expectedExample = new Example();
        expectedExample.setValue(CONTENT);
        expectedExample.setDescription(DESCRIPTION);
        Map.Entry<String, Example> expectedEntry = new AbstractMap.SimpleEntry<>(exampleReference.getReferredName(), expectedExample);
        assertThat(openAPI.getComponents().getExamples()).isNotEmpty();
        assertThat(openAPI.getComponents().getExamples()).containsExactly(expectedEntry);
    }
}
