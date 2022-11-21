package org.rodnansol.openapi.extender.generator.io;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

/**
 * Resolves XML content.
 */
public class XmlContentResolver implements ContentResolver {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    @Override
    public byte[] resolveContent(byte[] content) throws IOException {
        Object object = XML_MAPPER.readValue(content, Object.class);
        return XML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object).getBytes();
    }

    @Override
    public boolean supportsContentType(String contentType) {
        return contentType.contains("xml");
    }
}
