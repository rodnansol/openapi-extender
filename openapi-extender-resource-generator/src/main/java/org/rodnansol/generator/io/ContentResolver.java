package org.rodnansol.generator.io;

import java.io.IOException;

public interface ContentResolver {

    byte[] resolveContent(byte[] content) throws IOException;

    boolean supportsContentType(String contentType);

}
