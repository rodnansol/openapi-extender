package org.rodnansol;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ResponseBodyExampleOperationExtenderActionTest {

    @TempDir
    Path tmpWorkingDir;

    @Mock
    Operation mockOperation;

    ResponseExampleOperationExtenderAction underTest;

    @Test
    void testExtendWith_ShouldNotModifyOperation_WhenFileNameDoesNotMatch(@TempDir Path tmpWorkingDir) {
        //Given
        File file = new File("temp-file");

        //When
        underTest = new ResponseExampleOperationExtenderAction(tmpWorkingDir.toAbsolutePath().toString());
        underTest.extendWith(mockOperation, file);

        //Then
        verifyNoInteractions(mockOperation);
    }

    @ParameterizedTest
    @MethodSource("nonExistentMediaTypeExtenderTestCases")
    void testExtendWith_ShouldAddExample_WhenFileNameMatchesAndOperationIsNotHavingRequestBody(ExampleTestCase testCase) throws IOException {
        //Given
        Operation operation = new Operation();
        File file = new File(tmpWorkingDir.toFile(), testCase.getFileName());
        file.createNewFile();
        String expectedText = "Hello OpenAPI Extender";
        IOUtil.writeText(expectedText, file);

        //When
        underTest = new ResponseExampleOperationExtenderAction(tmpWorkingDir.toAbsolutePath().toString());
        underTest.extendWith(operation, file);

        //Then
        assertExample(operation, testCase.getStatusCode(), expectedText, testCase.getMediaType(), testCase.getDescription());
    }

    static Stream<ExampleTestCase> nonExistentMediaTypeExtenderTestCases() {
        return Stream.of(
            new ExampleTestCase("200_Successful Response.json", "application/json", "200", "Successful Response"),
            new ExampleTestCase("200_Successful Response.xml", "application/xml", "200", "Successful Response")
        );
    }


    private static void assertExample(Operation operation, String statusCode, String expectedText, String expectedMediaType, String expectedDescription) {
        ApiResponses apiResponses = operation.getResponses();
        assertThat(apiResponses, notNullValue());
        ApiResponse apiResponse = apiResponses.get(statusCode);
        assertThat(apiResponse, notNullValue());
        Content content = apiResponse.getContent();
        assertThat(content, notNullValue());
        assertThat(content.containsKey(expectedMediaType), is(true));
        MediaType mediaType = content.get(expectedMediaType);
        Example example = mediaType.getExamples().get(expectedDescription);
        assertThat(example, notNullValue());
        assertThat(example.getValue(), is(expectedText));
        assertThat(example.getDescription(), is(expectedDescription));
    }

    static class ExampleTestCase {

        private final String fileName;
        private final String mediaType;
        private final String statusCode;
        private final String description;

        public ExampleTestCase(String fileName, String mediaType, String statusCode, String description) {
            this.fileName = fileName;
            this.mediaType = mediaType;
            this.statusCode = statusCode;
            this.description = description;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMediaType() {
            return mediaType;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getDescription() {
            return description;
        }
    }
}
