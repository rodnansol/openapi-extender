package org.rodnansol.openapi.extender.springdoc.description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract resource loader that can load a resource from the classpath.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
abstract class AbstractResourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResourceLoader.class);

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    protected byte[] loadResourceByPath(String resourcePath) {
        Assert.notNull(resourcePath, "resourcePath is NULL");
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream(resourcePath)) {
            if (resourceAsStream == null) {
                LOGGER.debug("Unable to load resource at path:[{}]", resourcePath);
                return new byte[0];
            }
            LOGGER.debug("Resource at path:[{}] is found and being loaded as byte array.", resourcePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            transferTo(resourceAsStream, out);
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.warn("Error during reading file at:[" + resourcePath + "]", e);
        }
        return new byte[0];
    }

    private void transferTo(InputStream inputStream, OutputStream out) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = inputStream.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
            out.write(buffer, 0, read);
        }
    }

}
