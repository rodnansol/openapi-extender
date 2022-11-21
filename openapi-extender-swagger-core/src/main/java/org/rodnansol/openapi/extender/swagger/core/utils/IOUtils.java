package org.rodnansol.openapi.extender.swagger.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to handle most common IO calls.
 */
public class IOUtils {

    public static final Map<String, String> MEDIA_TYPE;

    static {
        MEDIA_TYPE = new HashMap<>();
        MEDIA_TYPE.put("json", "application/json");
        MEDIA_TYPE.put("xml", "application/xml");
    }

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private IOUtils() {
    }

    public static String readFileContent(File file) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(Files.newInputStream(file.toPath()), output);
        return output.toString();
    }

    public static String readStreamContent(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(inputStream, output);
        return output.toString();
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }


    public static long copyLarge(final InputStream input, final OutputStream output)
        throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
