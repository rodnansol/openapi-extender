package org.rodnansol.openapi.extender.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rodnansol.openapi.extender.generator.ApiResponseExampleFileOutputResourceGenerator;
import org.rodnansol.openapi.extender.generator.ReportParams;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiResponseDocumentReporterTest {

    private static final String RESPONSE_CONTENT = "{\"name\":\"Hello OpenAPI Extender\"}";
    @Mock
    private ApiResponseExampleFileOutputResourceGenerator apiResponseExampleDocumenter;

    @Mock
    private MvcResult mvcResult;

    @Mock
    private MockHttpServletResponse response;

    @Test
    void testHandle_shouldCallGenerateResourcesWithGivenOperationName_whenOperationNameIsPreset() throws IOException {
        // Given
        when(response.getStatus()).thenReturn(200);
        when(response.getContentType()).thenReturn(MediaType.APPLICATION_JSON_VALUE);
        when(response.getContentAsByteArray()).thenReturn(RESPONSE_CONTENT.getBytes());
        when(mvcResult.getResponse()).thenReturn(response);

        // When
        ApiResponseDocumentReporter underTest = new ApiResponseDocumentReporter(null, "test-operation-name", null, apiResponseExampleDocumenter);
        underTest.handle(mvcResult);

        // Then
        ReportParams expectedReportParams = new ReportParams(null, "test-operation-name", 200, MediaType.APPLICATION_JSON_VALUE, RESPONSE_CONTENT.getBytes());
        verify(apiResponseExampleDocumenter).generateResources(expectedReportParams);
    }
}
