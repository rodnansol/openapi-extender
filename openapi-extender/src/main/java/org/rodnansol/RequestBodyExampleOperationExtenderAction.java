package org.rodnansol;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.rodnansol.IOUtils.MEDIA_TYPE;
import static org.rodnansol.IOUtils.copy;

/**
 *
 */
final class RequestBodyExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyExampleOperationExtenderAction.class);
    private static final Pattern REQUEST_EXAMPLE_PATTERN = Pattern.compile("(\\d+)_(.*).(json|xml)");
    private final String workingDirectory;

    public RequestBodyExampleOperationExtenderAction(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        LOGGER.debug("Using [{}] directory for requests", workingDirectory);
    }

    @Override
    public void extendWith(Operation operation, File file) {
        String fileName = file.getName();
        Matcher matcher = REQUEST_EXAMPLE_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            String statusCode = matcher.group(1);
            String description = matcher.group(2);
            String fileExtension = matcher.group(3);
            RequestBody requestBody = operation.getRequestBody() == null ? new RequestBody() : operation.getRequestBody();
            operation.setRequestBody(requestBody);
            Content content = requestBody.getContent() == null ? new Content() : requestBody.getContent();
            requestBody.setContent(content);
            String rawMediaType = MEDIA_TYPE.get(fileExtension);
            MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
            if (!content.containsKey(rawMediaType)) {
                content.addMediaType(rawMediaType, mediaType);
            }
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                copy(new FileInputStream(file), output);
                Example example = new Example();
                String desc = description + " - Returns:" + statusCode;
                example.setDescription(desc);
                example.setValue(output.toString());
                mediaType.addExamples(desc, example);
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
