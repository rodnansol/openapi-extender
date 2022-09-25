package org.rodnansol.generator.io;

import org.rodnansol.generator.UnknownContentTypeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Class that deals with the IO operations.
 */
public class FileWriter {

    public static final FileWriter INSTANCE = new FileWriter();

    private static final List<ContentResolver> CONTENT_RESOLVERS = Arrays.asList(new JsonContentResolver(), new XmlContentResolver());

    private FileWriter() {
    }

    /**
     * Write byte array content to the given path.
     *
     * @param path        path where the content should be written.
     * @param content     content to be written to the path.
     * @param contentType type of the content.
     * @throws UnknownContentTypeException when the content type is unknown.
     */
    public void writeToFile(Path path, byte[] content, String contentType) throws IOException {
        boolean foundResolver = false;
        for (ContentResolver contentResolver : CONTENT_RESOLVERS) {
            if (contentResolver.supportsContentType(contentType)) {
                Files.write(path, contentResolver.resolveContent(content));
                foundResolver = true;
                break;
            }
        }
        if (foundResolver == false) {
            throw new UnknownContentTypeException("Content-type is unknown:[" + contentType + "]");
        }
    }

}
