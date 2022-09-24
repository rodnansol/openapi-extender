package org.rodnansol.core.openapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.internal.util.io.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.rodnansol.core.openapi.ExampleReferenceType.REQUEST;

class FileNameBasedExtenderResourceReaderTest {

    @TempDir
    Path tmpWorkingDir;

    FileNameBasedExampleResourceReader underTest;

    @Test
    void testExtendWith_ShouldNotModifyOperation_WhenFileNameDoesNotMatch() throws IOException {
        //Given
        File file = new File(tmpWorkingDir.toFile(), "temp-file");
        file.createNewFile();

        //When
        underTest = new FileNameBasedExampleResourceReader();
        ExampleReference result = underTest.createExampleReference(REQUEST, new ExtenderResource("temp-file", new ByteArrayInputStream(new byte[]{})));

        //Then
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @MethodSource("fileNameResolversCase")
    void testExtendWith_ShouldAddExample_WhenFileNameMatchesAndOperationIsNotHavingRequestBody(ExampleTestCase testCase) throws IOException {
        //Given
        File file = new File(tmpWorkingDir.toFile(), testCase.getFileName());
        String expectedText = "Hello OpenAPI Extender";
        IOUtil.writeText(expectedText, file);
        InputStream content = Files.newInputStream(file.toPath());
        ExtenderResource extenderResource = new ExtenderResource(testCase.getFileName(), content);

        //When
        underTest = new FileNameBasedExampleResourceReader();
        ExampleReference result = underTest.createExampleReference(REQUEST, extenderResource);

        //Then
        assertExampleReference(result, testCase);
    }

    private void assertExampleReference(ExampleReference exampleReference, ExampleTestCase testCase) {
        assertThat(exampleReference.getOperationId()).isEqualTo(testCase.getExpectedOperationId());
        assertThat(exampleReference.getStatusCode()).isEqualTo(testCase.getExpectedStatusCode());
        assertThat(exampleReference.getName()).isEqualTo(testCase.getExpectedName());
        assertThat(exampleReference.getDescription()).isEqualTo(testCase.getExpectedDescription());
        assertThat(exampleReference.getMediaType()).isEqualTo(testCase.getExpectedMediaType());
    }

    static Stream<ExampleTestCase> fileNameResolversCase() {
        return Stream.of(
            new ExampleTestCase("getUser_200_Test after midnight__Optional description.json", "getUser", "200", "Test after midnight", "Optional description", "application/json"),
            new ExampleTestCase("getUser_200_Test after midnight.json", "getUser", "200", "Test after midnight", "", "application/json")
        );
    }

    static class ExampleTestCase {

        private final String fileName;
        private final String expectedOperationId;
        private final String expectedStatusCode;
        private final String expectedName;
        private final String expectedDescription;
        private final String expectedMediaType;

        public ExampleTestCase(String fileName, String expectedOperationId, String expectedStatusCode, String expectedName, String expectedDescription, String expectedMediaType) {
            this.fileName = fileName;
            this.expectedOperationId = expectedOperationId;
            this.expectedStatusCode = expectedStatusCode;
            this.expectedName = expectedName;
            this.expectedDescription = expectedDescription;
            this.expectedMediaType = expectedMediaType;
        }

        public String getFileName() {
            return fileName;
        }

        public String getExpectedOperationId() {
            return expectedOperationId;
        }

        public String getExpectedStatusCode() {
            return expectedStatusCode;
        }

        public String getExpectedName() {
            return expectedName;
        }

        public String getExpectedDescription() {
            return expectedDescription;
        }

        public String getExpectedMediaType() {
            return expectedMediaType;
        }
    }

}
