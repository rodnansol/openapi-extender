package org.rodnansol.openapi.extender.swagger.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReference;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceContext;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceKey;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Action class that extends the {@link Operation}'s api response part with examples.
 */
public final class ResponseExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExampleOperationExtenderAction.class);

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();
    private static final BiConsumer<Example, ExampleReference> DEFAULT_CUSTOMIZER = (example, exampleReference) -> {
        example.setDescription(exampleReference.getDescription());
        example.set$ref(exampleReference.getReferredName());
    };

    private final BiConsumer<Example, ExampleReference> customizer;

    public ResponseExampleOperationExtenderAction() {
        this(DEFAULT_CUSTOMIZER);
    }

    public ResponseExampleOperationExtenderAction(BiConsumer<Example, ExampleReference> customizer) {
        this.customizer = customizer;
    }

    /**
     * Extends the {@link Operation}'s API response part with examples from the context.
     *
     * @param operation to be extended  from the context.
     */
    public void extendWith(final Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("operation is NULL");
        }
        String operationId = operation.getOperationId();
        LOGGER.debug("Extending operation's API response part with examples, operationId:[{}]", operationId);
        List<ExampleReference> references = exampleReferenceContext.getReference(new ExampleReferenceKey(operationId, ExampleReferenceType.RESPONSE));
        if (references == null || references.isEmpty()) {
            return;
        }
        extendWith(operation, references);
    }

    private void extendWith(Operation operation, List<ExampleReference> references) {
        references.forEach(exampleReference -> {
            String statusCode = exampleReference.getStatusCode();
            ApiResponses responses = operation.getResponses() == null ? new ApiResponses() : operation.getResponses();
            operation.setResponses(responses);
            ApiResponse apiResponse = responses.getOrDefault(statusCode, new ApiResponse());
            if (!responses.containsKey(statusCode)) {
                responses.addApiResponse(statusCode, apiResponse);
            }
            Content content = apiResponse.getContent() == null ? new Content() : apiResponse.getContent();
            apiResponse.setContent(content);
            String rawMediaType = exampleReference.getMediaType();
            MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
            if (!content.containsKey(rawMediaType)) {
                content.addMediaType(rawMediaType, mediaType);
            }
            Example example = new Example();
            customizer.accept(example, exampleReference);
            mediaType.addExamples(exampleReference.getName(), example);
        });

    }

}
