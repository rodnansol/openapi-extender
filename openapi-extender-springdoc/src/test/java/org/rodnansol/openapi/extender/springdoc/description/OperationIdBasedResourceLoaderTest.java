package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationIdBasedResourceLoaderTest {

    private static final String EXPECTED_RESOURCE_CONTENT = "Hello World\n";

    @Mock
    Function<LoadResourceCommand, Stream<String>> pathResolverFunction;
    OperationIdBasedResourceLoader underTest;

    @Test
    void loadResource_shouldLoadResourceResource_whenResourceExistsAndOperationIsGiven() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        LoadResourceCommand loadResourceCommand = new LoadResourceCommand("operations", ".md", operation, null);
        when(pathResolverFunction.apply(loadResourceCommand)).thenReturn(Stream.of("/operations/testOperation.md"));
        // When
        byte[] bytes = new OperationIdBasedResourceLoader(pathResolverFunction).loadResource(loadResourceCommand);

        // Then
        Assertions.assertThat(bytes)
            .isEqualTo(EXPECTED_RESOURCE_CONTENT.getBytes());
    }

}
