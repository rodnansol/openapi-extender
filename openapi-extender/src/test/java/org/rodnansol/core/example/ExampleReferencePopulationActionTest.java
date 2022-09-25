package org.rodnansol.core.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.resource.ExampleReferenceCreationResult;
import org.rodnansol.core.resource.ExampleResourceReader;
import org.rodnansol.core.resource.ExtenderResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExampleReferencePopulationActionTest {

    private static final String OPERATION_NAME = "testOperation";
    @Mock
    ExampleReferenceContext exampleReferenceContext;

    @Mock
    ExampleResourceReader exampleResourceReader;

    @InjectMocks
    ExampleReferencePopulationAction underTest;

    @Test
    void populateExampleReferenceContext_ShouldThrowException_WhenIncomingResourcesAreNull() {
        // Given

        // When
        assertThrows(IllegalArgumentException.class, () -> underTest.populateExampleReferenceContext(ExampleReferenceType.REQUEST, null));

        // Then
    }

    @Test
    void populateExampleReferenceContext_ShouldPopulateExampleReferenceContext_WhenCalledWithAListOfResourcesThatCanBeProcessed() {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        String name = "resource-name";
        ExtenderResource extenderResource = new ExtenderResource(name, inputStream);
        List<ExtenderResource> resources = new ArrayList<>();
        resources.add(extenderResource);
        List<ExampleReference> references = new ArrayList<>();
        ExampleReference exampleReference = new ExampleReference(OPERATION_NAME, "200", "application/json", "test", ExampleReferenceType.REQUEST);
        references.add(exampleReference);
        when(exampleResourceReader.createExampleReferences(ExampleReferenceType.REQUEST, extenderResource)).thenReturn(new ExampleReferenceCreationResult(OPERATION_NAME, references));

        // When
        underTest.populateExampleReferenceContext(ExampleReferenceType.REQUEST, resources);

        // Then
        verify(exampleReferenceContext).addReference(new ExampleReferenceKey(OPERATION_NAME, ExampleReferenceType.REQUEST), exampleReference);
    }

    @Test
    void populateExampleReferenceContext_ShouldNotPopulateExampleReferenceContext_WhenResourceRederReturnsNullResult() {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        String name = "resource-name";
        ExtenderResource extenderResource = new ExtenderResource(name, inputStream);
        List<ExtenderResource> resources = new ArrayList<>();
        resources.add(extenderResource);
        when(exampleResourceReader.createExampleReferences(ExampleReferenceType.REQUEST, extenderResource)).thenReturn(null);

        // When
        underTest.populateExampleReferenceContext(ExampleReferenceType.REQUEST, resources);

        // Then
        verifyNoInteractions(exampleReferenceContext);
    }

    @Test
    void populateExampleReferenceContext_ShouldNotPopulateExampleReferenceContext_WhenResourceRederReturnsProperResultButWithEmptyList() {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        String name = "resource-name";
        ExtenderResource extenderResource = new ExtenderResource(name, inputStream);
        List<ExtenderResource> resources = new ArrayList<>();
        resources.add(extenderResource);
        when(exampleResourceReader.createExampleReferences(ExampleReferenceType.REQUEST, extenderResource)).thenReturn(new ExampleReferenceCreationResult(OPERATION_NAME, Collections.emptyList()));

        // When
        underTest.populateExampleReferenceContext(ExampleReferenceType.REQUEST, resources);

        // Then
        verifyNoInteractions(exampleReferenceContext);
    }
}
