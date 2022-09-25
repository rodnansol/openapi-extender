package org.rodnansol.generator.io;

import java.io.IOException;

/**
 * Interface for different content resolver implementations.
 */
public interface ContentResolver {

    /**
     * Resolves the content from the given byte array and returns in another one.
     *
     * @param content to be read.
     * @return new content.
     * @throws IOException when the content can not be read.
     */
    byte[] resolveContent(byte[] content) throws IOException;

    /**
     * Checks if the incoming content type is supported or not.
     *
     * @param contentType content type.
     * @return returns <b>true</b> if the content type is supported, otherwise <b>false</b>.
     */
    boolean supportsContentType(String contentType);

}
