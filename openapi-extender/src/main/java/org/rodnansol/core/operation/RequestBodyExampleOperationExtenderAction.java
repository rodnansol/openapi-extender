package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
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
public final class RequestBodyExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyExampleOperationExtenderAction.class);
    private static final Pattern REQUEST_EXAMPLE_PATTERN = Pattern.compile("(\\d+)_(.*).(json|xml)");
    private static final BiConsumer<Example, RequestBodyMatcherResult> DEFAULT_CUSTOMIZER = (example, matcherResult) -> {
        String desc = matcherResult.getDescription() + " - Returns: HTTP " + matcherResult.getStatusCode();
        example.setDescription(desc);
        example.setValue(matcherResult.getExampleContent());
    };
    private final String workingDirectory;
    private final BiConsumer<Example, RequestBodyMatcherResult> customizer;

    public RequestBodyExampleOperationExtenderAction(String workingDirectory) {
        this(workingDirectory, DEFAULT_CUSTOMIZER);
    }

    public RequestBodyExampleOperationExtenderAction(String workingDirectory, BiConsumer<Example, RequestBodyMatcherResult> customizer) {
        this.workingDirectory = workingDirectory;
        this.customizer = customizer;
        LOGGER.debug("Using [{}] directory for requests", workingDirectory);
    }

    @Override
    public void extendWith(Operation operation, File file) {
        String fileName = file.getName();
        Matcher matcher = REQUEST_EXAMPLE_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            RequestBodyMatcherResult matcherResult;
            try {
                matcherResult = new RequestBodyMatcherResult(matcher, file);
            } catch (IOException e) {
                throw new ExtenderActionException("Error during matching file", e);
            }
            extendWith(operation, matcherResult);
        }
    }

    public void extendWith(Operation operation, RequestBodyMatcherResult matcherResult) {
        RequestBody requestBody = operation.getRequestBody() == null ? new RequestBody() : operation.getRequestBody();
        operation.setRequestBody(requestBody);
        Content content = requestBody.getContent() == null ? new Content() : requestBody.getContent();
        requestBody.setContent(content);
        String rawMediaType = MEDIA_TYPE.get(matcherResult.getExtension());
        MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
        if (!content.containsKey(rawMediaType)) {
            content.addMediaType(rawMediaType, mediaType);
        }
        Example example = new Example();
        customizer.accept(example, matcherResult);
        mediaType.addExamples(example.getDescription(), example);
    }

    @Override
    public String workingDirectory() {
        return workingDirectory;
    }

}
