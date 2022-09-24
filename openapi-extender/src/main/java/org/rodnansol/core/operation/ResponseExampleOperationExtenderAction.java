package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.rodnansol.core.utils.IOUtils.MEDIA_TYPE;

/**
 *
 */
public final class ResponseExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExampleOperationExtenderAction.class);

    private static final Pattern RESPONSE_EXAMPLE = Pattern.compile("(\\d+)_(.*).(json|xml)");
    private static final BiConsumer<Example, ResponseMatcherResult> DEFAULT_CUSTOMIZER = (example, matcherResult) -> {
        example.setDescription(matcherResult.getDescription());
        example.setValue(matcherResult.getExampleContent());
    };

    private final String workingDirectory;
    private final BiConsumer<Example, ResponseMatcherResult> customizer;

    public ResponseExampleOperationExtenderAction(String workingDirectory) {
        this(workingDirectory, DEFAULT_CUSTOMIZER);
    }

    public ResponseExampleOperationExtenderAction(String workingDirectory, BiConsumer<Example, ResponseMatcherResult> customizer) {
        this.workingDirectory = workingDirectory;
        this.customizer = customizer;
        LOGGER.debug("Using [{}] directory for responses", workingDirectory);
    }

    @Override
    public void extendWith(final Operation operation, File file) {
        String fileName = file.getName();
        Matcher responseExampleMatcher = RESPONSE_EXAMPLE.matcher(fileName);

        if (responseExampleMatcher.matches()) {
            ResponseMatcherResult responseMatcherResult;
            try {
                responseMatcherResult = new ResponseMatcherResult(responseExampleMatcher, file);
            } catch (IOException e) {
                throw new ExtenderActionException("Error during matching file", e);
            }
            extendWith(operation, responseMatcherResult);
        }

    }

    public void extendWith(Operation operation, ResponseMatcherResult responseMatcherResult) {
        String statusCode = responseMatcherResult.getStatusCode();
        ApiResponses responses = operation.getResponses() == null ? new ApiResponses() : operation.getResponses();
        operation.setResponses(responses);
        ApiResponse apiResponse = responses.getOrDefault(statusCode, new ApiResponse());
        if (!responses.containsKey(statusCode)) {
            responses.addApiResponse(statusCode, apiResponse);
        }
        Content content = apiResponse.getContent() == null ? new Content() : apiResponse.getContent();
        apiResponse.setContent(content);
        String rawMediaType = MEDIA_TYPE.get(responseMatcherResult.getExtension());
        MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
        if (!content.containsKey(rawMediaType)) {
            content.addMediaType(rawMediaType, mediaType);
        }
        Example example = new Example();
        customizer.accept(example, responseMatcherResult);
        mediaType.addExamples(example.getDescription(), example);
    }

    @Override
    public String workingDirectory() {
        return workingDirectory;
    }
}
