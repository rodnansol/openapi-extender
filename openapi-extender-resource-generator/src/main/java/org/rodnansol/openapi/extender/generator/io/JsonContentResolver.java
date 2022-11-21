package org.rodnansol.openapi.extender.generator.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Resolves JSON content.
 */
public class JsonContentResolver implements ContentResolver{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public byte[] resolveContent(byte[] content) throws IOException {
        Object object = OBJECT_MAPPER.readValue(content, Object.class);
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object).getBytes();
    }

    @Override
    public boolean supportsContentType(String contentType) {
        return contentType.contains("json");
    }
}
