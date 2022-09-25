package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
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
class ResponseBodyExampleOperationExtenderActionTest {

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();

    ResponseExampleOperationExtenderAction underTest;

    @Test
    void testExtendWith_ShouldNotModifyOperation_WhenFileNameDoesNotMatch() {
        //Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");

        //When
        underTest = new ResponseExampleOperationExtenderAction();
        underTest.extendWith(operation);

        //Then
        assertThat(operation.getResponses()).isNullOrEmpty();
    }

    @ParameterizedTest
    @MethodSource("nonExistentMediaTypeExtenderTestCases")
    void testExtendWith_ShouldAddExample_WhenFileNameMatchesAndOperationIsNotHavingRequestBody(ExampleReference exampleReference) throws IOException {
        //Given
        Operation operation = new Operation();
        operation.setOperationId(exampleReference.getOperationId());
        exampleReferenceContext.addReference(new ExampleReferenceKey(exampleReference.getOperationId(), ExampleReferenceType.RESPONSE), exampleReference);

        //When
        underTest = new ResponseExampleOperationExtenderAction();
        underTest.extendWith(operation);

        //Then
        assertExample(operation, exampleReference);
    }

    static Stream<ExampleReference> nonExistentMediaTypeExtenderTestCases() {
        return Stream.of(
            ExampleReference.builder()
                .operationId("testOperationId")
                .statusCode("200")
                .type(ExampleReferenceType.RESPONSE)
                .mediaType("application/json")
                .name("Test")
                .description("Optional description")
                .build(),
            ExampleReference.builder()
                .operationId("testOperationId")
                .statusCode("200")
                .type(ExampleReferenceType.RESPONSE)
                .mediaType("application/json")
                .name("Test")
                .build()
        );
    }

    private static void assertExample(Operation operation, ExampleReference exampleReference) {
        ApiResponses apiResponses = operation.getResponses();
        assertThat(apiResponses).isNotNull();
        ApiResponse apiResponse = apiResponses.get(exampleReference.getStatusCode());
        assertThat(apiResponse).isNotNull();
        Content content = apiResponse.getContent();
        assertThat(content).isNotNull();
        assertThat(content).containsKey(exampleReference.getMediaType());
        MediaType mediaType = content.get(exampleReference.getMediaType());
        Example example = mediaType.getExamples().get(exampleReference.getName());
        assertThat(example).isNotNull();
        assertThat(example.getDescription()).isEqualTo(exampleReference.getDescription());
    }
}
