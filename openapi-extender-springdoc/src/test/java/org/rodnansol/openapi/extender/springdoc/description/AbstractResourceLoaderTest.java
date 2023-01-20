package org.rodnansol.openapi.extender.springdoc.description;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AbstractResourceLoaderTest {

    AbstractResourceLoader underTest = new TestImpl();

    @ParameterizedTest
    @ValueSource(strings = {
        "/operations/testOperation.md",
        "operations/testOperation.md"
    })
    void loadResourceByPath_shouldLoadResourceFromClasspathAndReturnInByteArray_whenResourceExist(String path) {
        // Given

        // When
        byte[] bytes = underTest.loadResourceByPath(path);

        // Then
        Assertions.assertThat(bytes)
            .isEqualTo("Hello World\n".getBytes());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/operations/non-existent.md",
        "operations/non-existent.md"
    })
    void loadResourceByPath_shouldReturnEmptyArray_whenResourceDoesNotExist(String path) {
        // Given

        // When
        byte[] bytes = underTest.loadResourceByPath(path);

        // Then
        Assertions.assertThat(bytes)
            .isEmpty();
    }

    class TestImpl extends AbstractResourceLoader {

    }
}


