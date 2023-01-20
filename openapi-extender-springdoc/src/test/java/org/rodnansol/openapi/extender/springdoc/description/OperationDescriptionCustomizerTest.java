package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationDescriptionCustomizerTest {

    @Mock
    ExecutorService executorService;
    @Mock
    OperationDescriptionConfiguration configuration;
    @Mock
    OperationDescriptionLoaderService operationDescriptionLoaderService;

    @InjectMocks
    OperationDescriptionCustomizer underTest;

    @Test
    void customize_shouldSkipInjection_whenOperationIsBlacklisted() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        underTest.setOperationIdBlackList(Stream.of("testOperation").collect(Collectors.toList()));
        // When
        underTest.customize(operation, null);

        // Then
        verifyNoMoreInteractions(operationDescriptionLoaderService);
    }

    @Test
    void customize_shouldSkipInjection_whenOperationIsNotWhitelisted() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        underTest.setOperationIdWhiteList(new ArrayList<>());
        // When
        underTest.customize(operation, null);

        // Then
        verifyNoMoreInteractions(operationDescriptionLoaderService);
    }

    @Test
    void customize_shouldExecuteInjection_whenNotBlacklistedNorWhitelisted() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        when(configuration.getResourcesBasePath()).thenReturn("testBasePath");
        when(configuration.getExtension()).thenReturn(".md");

        // When
        underTest.customize(operation, handlerMethod);

        // Then
        verify(operationDescriptionLoaderService).injectDescriptions("testBasePath", ".md", operation, handlerMethod);
        verify(operationDescriptionLoaderService).injectSummary("testBasePath", ".md", operation, handlerMethod);
    }

    @Test
    void customize_shouldExecuteInjection_whenNotBlacklistedButWhitelisted() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        underTest.setOperationIdWhiteList(Stream.of("testOperation").collect(Collectors.toList()));
        when(configuration.getResourcesBasePath()).thenReturn("testBasePath");
        when(configuration.getExtension()).thenReturn(".md");

        // When
        underTest.customize(operation, handlerMethod);

        // Then
        verify(operationDescriptionLoaderService).injectDescriptions("testBasePath", ".md", operation, handlerMethod);
        verify(operationDescriptionLoaderService).injectSummary("testBasePath", ".md", operation, handlerMethod);
    }

    @Test
    void customize_shouldExecuteInjectionInBackground_whenSetToRunInBackground() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        when(configuration.getResourcesBasePath()).thenReturn("testBasePath");
        when(configuration.getExtension()).thenReturn(".md");

        // When
        underTest.setInBackground(true);
        underTest.customize(operation, handlerMethod);

        // Then
        ArgumentCaptor<Runnable> runnable = ArgumentCaptor.forClass(Runnable.class);
        verify(executorService).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();
        verify(operationDescriptionLoaderService).injectDescriptions("testBasePath", ".md", operation, handlerMethod);
        verify(operationDescriptionLoaderService).injectSummary("testBasePath", ".md", operation, handlerMethod);
    }
}
