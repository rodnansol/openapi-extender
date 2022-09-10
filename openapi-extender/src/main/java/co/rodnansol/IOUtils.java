package co.rodnansol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class IOUtils {

    static final Map<String, String> MEDIA_TYPE;

    static {
        MEDIA_TYPE = new HashMap<>();
        MEDIA_TYPE.put("json", "application/json");
        MEDIA_TYPE.put("xml", "application/xml");
    }

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }


    static long copyLarge(final InputStream input, final OutputStream output)
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
