package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

class CustomExtensionBasedResourceLoaderTest {

    private static final String EXPECTED_RESOURCE_CONTENT = "Hello World\n";
    protected static final String TEST_PROPERTY = "testProperty";

    @Test
    void loadResource_shouldLoadResourceResource_whenResourceExistsAndOperationIsGiven() {
        // Given
        Operation operation = new Operation();
        operation.setOperationId("testOperation");
        operation.addExtension(OpenApiExtenderExtensionConstants.EXTENSION_KEY, Maps.newHashMap(TEST_PROPERTY,"operations/testOperation.md"));
        LoadResourceCommand loadResourceCommand = new LoadResourceCommand("operations", ".md", operation, null);
        // When
        byte[] bytes = new CustomExtensionBasedResourceLoader(TEST_PROPERTY).loadResource(loadResourceCommand);

        // Then
        Assertions.assertThat(bytes)
            .isEqualTo(EXPECTED_RESOURCE_CONTENT.getBytes());
    }

}
