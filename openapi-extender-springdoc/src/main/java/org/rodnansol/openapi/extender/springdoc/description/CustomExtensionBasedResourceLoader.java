package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Resource loader implementation that tries to resolve the resources based on the {@link io.swagger.v3.oas.annotations.Operation} annotation's {@link Operation#extensions()} list.
 * <p>
 * Custom extensions are supported by the OpenAPI specs, and this class is reading those values.
 * <p>
 *
 * <pre>
 * {@code
 *     @Operation(extensions = {
 *         @Extension(name = OpenApiExtenderExtensionConstants.EXTENSION_KEY,
 *             properties = {
 *                 @ExtensionProperty(name = OpenApiExtenderExtensionConstants.DESCRIPTION_KEY, value = "operations/anotherDescription.md"),
 *                 @ExtensionProperty(name = OpenApiExtenderExtensionConstants.SUMMARY_KEY, value = "operations/anotherSummary.md")}
 *         )
 *     })
 *     @PutMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
 *     public ResponseEntity putUser(@RequestParam(name = "id", required = false) String id) {
 *         return ResponseEntity.ok();
 *     }
 * }
 *
 * </pre>
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
class CustomExtensionBasedResourceLoader extends AbstractResourceLoader implements ResourceLoader {

    private final String propertyKey;

    CustomExtensionBasedResourceLoader(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    @Override
    public byte[] loadResource(LoadResourceCommand loadResourceCommand) {
        Assert.notNull(loadResourceCommand, "operation is NULL");
        Assert.notNull(loadResourceCommand.getOperation(), "operation is NULL");
        Map<String, Object> extensions = loadResourceCommand.getOperation().getExtensions();
        if (extensions != null) {
            Object extensionMap = extensions.get(OpenApiExtenderExtensionConstants.EXTENSION_KEY);
            if (extensionMap instanceof HashMap) {
                Object property = ((HashMap<?, ?>) extensionMap).get(propertyKey);
                if (property instanceof String) {
                    return loadResourceByPath((String) property);
                }
            }
        }
        return new byte[0];
    }

}
