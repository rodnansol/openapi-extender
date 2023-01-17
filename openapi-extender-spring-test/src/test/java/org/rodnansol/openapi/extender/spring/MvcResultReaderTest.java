package org.rodnansol.openapi.extender.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MvcResultReaderTest {

    @Mock
    MvcResult result;
    @Mock
    HandlerMethod handlerMethod;
    @Mock
    Method method;
    @Mock
    MockOperation operation;

    @Test
    void getOperationName_shouldReturnOperationName_whenOperationIsNotNull() {
        // Given

        // When
        String operationName = MvcResultReader.getOperationName("test-operation", null);

        // Then
        assertThat(operationName).isEqualTo("test-operation");
    }

    @Test
    void getOperationName_shouldReturnNameOfTheHandlerMethod_whenNoOperationAnnotationIsSpecifiedAndOperationParamIsNull() {
        // Given
        when(method.getName()).thenReturn("testMethod");
        when(handlerMethod.getMethod()).thenReturn(method);
        when(result.getHandler()).thenReturn(handlerMethod);

        // When
        String operationName = MvcResultReader.getOperationName(null, result);

        // Then
        assertThat(operationName).isEqualTo("testMethod");
    }

    @Test
    void getOperationName_shouldReturnTheValueOfTheOperationAnnotation_whenOperationAnnotationIsPresentAndOperationParamIsNull() {
        // Given
        when(operation.operationId()).thenReturn("testOperation");
        when(handlerMethod.getMethodAnnotation(Operation.class)).thenReturn(operation);
        when(handlerMethod.getMethod()).thenReturn(method);
        when(result.getHandler()).thenReturn(handlerMethod);

        // When
        String operationName = MvcResultReader.getOperationName(null, result);

        // Then
        assertThat(operationName).isEqualTo("testOperation");
    }

    class MockOperation implements Operation {

        @Override
        public String method() {
            return null;
        }

        @Override
        public String[] tags() {
            return new String[0];
        }

        @Override
        public String summary() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public RequestBody requestBody() {
            return null;
        }

        @Override
        public ExternalDocumentation externalDocs() {
            return null;
        }

        @Override
        public String operationId() {
            return null;
        }

        @Override
        public Parameter[] parameters() {
            return new Parameter[0];
        }

        @Override
        public ApiResponse[] responses() {
            return new ApiResponse[0];
        }

        @Override
        public boolean deprecated() {
            return false;
        }

        @Override
        public SecurityRequirement[] security() {
            return new SecurityRequirement[0];
        }

        @Override
        public Server[] servers() {
            return new Server[0];
        }

        @Override
        public Extension[] extensions() {
            return new Extension[0];
        }

        @Override
        public boolean hidden() {
            return false;
        }

        @Override
        public boolean ignoreJsonView() {
            return false;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}
