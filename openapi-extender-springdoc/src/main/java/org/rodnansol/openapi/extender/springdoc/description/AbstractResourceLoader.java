package org.rodnansol.openapi.extender.springdoc.description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class AbstractResourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResourceLoader.class);

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    protected byte[] loadResource(String resourceBasePath, String extension, String resourcePostfix, String resourceName) {
        String filePath = String.format("/%s/%s%s%s", resourceBasePath, resourceName, resourcePostfix, extension);
        return loadResourceByPath(filePath);
    }

    protected byte[] loadResourceByPath(String filePath) {
        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream(filePath)) {
            if (resourceAsStream == null) {
                LOGGER.debug("Unable to load resource at path:[{}]", filePath);
                return new byte[0];
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            transferTo(resourceAsStream, out);
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.warn("Error during reading file at:[" + filePath + "]", e);
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
