package org.rodnansol.openapi.extender.springdoc.description;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodNameBasedResourceLoaderTest {

    protected static final String EXPECTED_RESOURCE_CONTENT = "Hello World\n";

    @Mock
    Function<LoadResourceCommand, Stream<String>> pathResolverFunction;

    @Test
    void loadResource_shouldLoadResourceResource_whenResourceExistsAndOperationIsGiven() throws NoSuchMethodException {
        // Given
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        LoadResourceCommand loadResourceCommand = new LoadResourceCommand("operations", ".md", null, handlerMethod);
        when(pathResolverFunction.apply(loadResourceCommand)).thenReturn(Stream.of("/operations/testOperation.md"));
        // When
        byte[] bytes = new MethodNameBasedResourceLoader(pathResolverFunction).loadResource(loadResourceCommand);

        // Then
        Assertions.assertThat(bytes)
            .isEqualTo(EXPECTED_RESOURCE_CONTENT.getBytes());
    }

}
