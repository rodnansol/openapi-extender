package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.core.example.ExampleReference;
import org.rodnansol.core.example.ExampleReferenceContext;
import org.rodnansol.core.example.ExampleReferenceKey;
import org.rodnansol.core.example.ExampleReferenceType;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RequestBodyExampleOperationExtenderActionTest {

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();

    RequestBodyExampleOperationExtenderAction underTest;

    @AfterEach
    void tearDown() {
        exampleReferenceContext.clearContext();
    }

    @Test
    void testExtendWith_ShouldNotModifyOperation_WhenContextIsEmpty() {
        //Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");

        //When
        underTest = new RequestBodyExampleOperationExtenderAction();
        underTest.extendWith(operation);

        //Then
        assertThat(operation.getRequestBody()).isNull();
    }

    @ParameterizedTest
    @MethodSource("nonExistentMediaTypeExtenderTestCases")
    void testExtendWith_ShouldAddExample_WhenContextIsHavingReferences(ExampleReference exampleReference) throws IOException {
        //Given
        Operation operation = new Operation();
        operation.setOperationId(exampleReference.getOperationId());
        exampleReferenceContext.addReference(new ExampleReferenceKey(exampleReference.getOperationId(), ExampleReferenceType.REQUEST), exampleReference);

        //When
        underTest = new RequestBodyExampleOperationExtenderAction();
        underTest.extendWith(operation);

        //Then
        assertExample(operation, exampleReference);
    }

    static Stream<ExampleReference> nonExistentMediaTypeExtenderTestCases() {
        return Stream.of(
            ExampleReference.builder()
                .operationId("testOperationId")
                .statusCode("200")
                .type(ExampleReferenceType.REQUEST)
                .mediaType("application/json")
                .name("Test")
                .description("Optional description")
                .build(),
            ExampleReference.builder()
                .operationId("testOperationId")
                .statusCode("200")
                .type(ExampleReferenceType.REQUEST)
                .mediaType("application/json")
                .name("Test")
                .build()
        );
    }

    private static void assertExample(Operation operation, ExampleReference exampleReference) {
        RequestBody requestBody = operation.getRequestBody();
        assertThat(requestBody).isNotNull();
        Content content = requestBody.getContent();
        assertThat(content).isNotNull();
        assertThat(content).containsKey(exampleReference.getMediaType());
        MediaType mediaTypes = content.get(exampleReference.getMediaType());
        Example example = mediaTypes.getExamples().get(exampleReference.getName());
        assertThat(example).isNotNull();
        assertThat(example.get$ref()).isEqualTo("#/components/examples/" + exampleReference.getReferredName());
        if (exampleReference.getDescription() != null && !exampleReference.getDescription().trim().equals("")) {
            assertThat(example.getDescription()).isEqualTo(exampleReference.getDescription() + " - Returns: HTTP " + exampleReference.getStatusCode());
        } else {
            assertThat(example.getDescription()).isEqualTo("Returns: HTTP " + exampleReference.getStatusCode());
        }
    }
}
