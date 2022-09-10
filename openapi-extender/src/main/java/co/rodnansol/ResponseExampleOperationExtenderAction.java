package co.rodnansol;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.rodnansol.IOUtils.MEDIA_TYPE;
import static co.rodnansol.IOUtils.copy;

/**
 *
 */
final class ResponseExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExampleOperationExtenderAction.class);

    private static final Pattern RESPONSE_EXAMPLE = Pattern.compile("(\\d+)_(.*).(json|xml)");

    private final String workingDirectory;

    public ResponseExampleOperationExtenderAction(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        LOGGER.debug("Using [{}] directory for responses", workingDirectory);
    }

    @Override
    public void extendWith(final Operation operation, File file) {
        String fileName = file.getName();
        Matcher responseExampleMatcher = RESPONSE_EXAMPLE.matcher(fileName);
        if (responseExampleMatcher.matches()) {
            String statusCode = responseExampleMatcher.group(1);
            String description = responseExampleMatcher.group(2);
            String fileExtension = responseExampleMatcher.group(3);
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

    @Override
    public String workingDirectory() {
        return workingDirectory;
    }
}
