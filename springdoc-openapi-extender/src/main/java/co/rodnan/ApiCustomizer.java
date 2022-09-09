package co.rodnan;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCustomizer.class);
    private static final Map<String, String> MEDIA_TYPE;
    private static final Pattern PATTERN = Pattern.compile("(\\d+)_(.*).(json|xml)");
    public static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static final String TARGET_FOLDER = "openapi/";

    static {
        MEDIA_TYPE = new HashMap<>();
        MEDIA_TYPE.put("json", "application/json");
        MEDIA_TYPE.put("xml", "application/xml");
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String operationId = operation.getOperationId();
        LOGGER.debug("Extending operation with ID:[{}]", operationId);
        try {
            URL resource = getClass().getClassLoader().getResource(TARGET_FOLDER + operationId);
            File folder = new File(resource.getFile());
            File[] files = folder.listFiles();
            if (files == null) {
                return operation;
            }
            for (File file : files) {
                String fileName = file.getName();
                Matcher matcher = PATTERN.matcher(fileName);
                if (matcher.matches()) {
                    String statusCode = matcher.group(1);
                    String description = matcher.group(2);
                    String fileExtension = matcher.group(3);
                    ApiResponses responses = operation.getResponses();
                    ApiResponse apiResponse = responses.getOrDefault(statusCode, new ApiResponse());
                    if (!responses.containsKey(statusCode)) {
                        responses.addApiResponse(statusCode, apiResponse);
                    }
                    Content content = apiResponse.getContent() == null ? new Content() : apiResponse.getContent();
                    apiResponse.setContent(content);
                    String rawMediaType = MEDIA_TYPE.get(fileExtension);
                    MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
                    if (!content.containsKey(rawMediaType)) {
                        content.addMediaType(rawMediaType, mediaType);
                    }
                    try {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        copy(new FileInputStream(file), output);
                        Example example = new Example();
                        example.setDescription(description);
                        example.setValue(output.toString());
                        mediaType.addExamples(description, example);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        } catch (RuntimeException e) {
            LOGGER.error("Error during populating OpenAPI operation:[" + operationId + "] with extra elements", e);
        }
        return operation;
    }

    private int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }


    private long copyLarge(final InputStream input, final OutputStream output)
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
