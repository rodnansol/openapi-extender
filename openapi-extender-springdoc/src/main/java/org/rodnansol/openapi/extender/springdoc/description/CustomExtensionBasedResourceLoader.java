package org.rodnansol.openapi.extender.springdoc.description;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
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
