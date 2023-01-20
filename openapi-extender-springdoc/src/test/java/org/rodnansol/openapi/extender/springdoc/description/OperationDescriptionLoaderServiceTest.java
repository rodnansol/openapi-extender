package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OperationDescriptionLoaderServiceTest {

    Charset resourceCharset = StandardCharsets.UTF_8;
    List<ResourceLoader> descriptionResourceLoaders = new ArrayList<>();
    List<ResourceLoader> summaryResourceLoaders = new ArrayList<>();

    @Test
    void injectDescriptions_shouldSkipOverride_whenOperationAlreadyHasDescription() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        operation.setDescription("Already exists");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectDescriptions("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getDescription()).isEqualTo("Already exists");
    }

    @Test
    void injectDescriptions_shouldNotOverrideDescription_whenDescriptionDoesNotExistAndResourceLoaderDoesNotFindResource() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        descriptionResourceLoaders.add(new TestResourceLoader(false));

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectDescriptions("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getDescription()).isNull();
    }

    @Test
    void injectDescriptions_shouldOverrideSummary_whenSummaryoesNotExistAndResourceLoaderFindsResource() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        descriptionResourceLoaders.add(new TestResourceLoader(true));

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectDescriptions("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getDescription()).isEqualTo("Hello World");
    }

    @Test
    void injectSummary_shouldSkipSummaryOverride_whenOperationAlreadyHasSummary() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        operation.setSummary("Already exists");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectSummary("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getSummary()).isEqualTo("Already exists");
    }

    @Test
    void injectSummary_shouldNotOverrideSummary_whenSummaryDoesNotExistAndResourceLoaderDoesNotFindResource() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        summaryResourceLoaders.add(new TestResourceLoader(false));

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectSummary("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getSummary()).isNull();
    }

    @Test
    void injectSummary_shouldOverrideDescription_whenDescriptionDoesNotExistAndResourceLoaderFindsResource() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        summaryResourceLoaders.add(new TestResourceLoader(true));

        // When
        new OperationDescriptionLoaderService(resourceCharset, descriptionResourceLoaders, summaryResourceLoaders)
            .injectSummary("testBasePath", ".md", operation, handlerMethod);

        // Then
        assertThat(operation.getSummary()).isEqualTo("Hello World");
    }

    class TestResourceLoader implements ResourceLoader {

        private final boolean shouldLoad;

        TestResourceLoader(boolean shouldLoad) {
            this.shouldLoad = shouldLoad;
        }

        @Override
        public byte[] loadResource(LoadResourceCommand loadResourceCommand) {
            return shouldLoad ? "Hello World".getBytes() : new byte[0];
        }
    }
}

