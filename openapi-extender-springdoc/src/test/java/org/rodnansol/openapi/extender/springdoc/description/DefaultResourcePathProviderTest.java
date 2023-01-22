package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class DefaultResourcePathProviderTest {

    @Test
    void getOperationBasedFunction_shouldReturnOperationBasedPathFunction() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        LoadResourceCommand loadResourceCommand = new LoadResourceCommand("operations", ".md", operation, null);

        // When
        List<String> resultList = DefaultResourcePathProvider.getOperationBasedFunction("-postFix", DescriptorType.SUMMARY).apply(loadResourceCommand)
            .collect(Collectors.toList());

        // Then
        assertThat(resultList)
            .contains(
                "/operations/testOperation-postFix.md",
                "/operations/testOperation/summary.md"
            );
    }

    @Test
    void getMethodHandlerBasedFunction_shouldReturnMethodHandlerBasedFunction() throws NoSuchMethodException {
        // Given
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        doReturn(TestClass.class).when(handlerMethod).getBeanType();
        doReturn(TestClass.class.getMethod("testMethod")).when(handlerMethod).getMethod();
        LoadResourceCommand loadResourceCommand = new LoadResourceCommand("operations", ".md", null, handlerMethod);

        // When
        List<String> resultList = DefaultResourcePathProvider.getMethodHandlerBasedFunction("-postFix", DescriptorType.SUMMARY).apply(loadResourceCommand)
            .collect(Collectors.toList());

        // Then
        assertThat(resultList)
            .contains(
                "/operations/org.rodnansol.openapi.extender.springdoc.description.TestClass#testMethod-postFix.md",
                "/operations/org.rodnansol.openapi.extender.springdoc.description.TestClass/testMethod-summary.md"
            );
    }


}

