package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.rodnansol.core.openapi.ExampleReference;
import org.rodnansol.core.openapi.ExampleReferenceContext;
import org.rodnansol.core.openapi.ExampleReferenceKey;
import org.rodnansol.core.openapi.ExampleReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 */
public final class ResponseExampleOperationExtenderActionV2 implements OperationExtenderActionV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExampleOperationExtenderActionV2.class);

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();
    private static final BiConsumer<Example, ExampleReference> DEFAULT_CUSTOMIZER = (example, exampleReference) -> {
        example.setDescription(exampleReference.getDescription());
        example.set$ref(exampleReference.getReferredName());
    };

    private final BiConsumer<Example, ExampleReference> customizer;

    public ResponseExampleOperationExtenderActionV2() {
        this(DEFAULT_CUSTOMIZER);
    }

    public ResponseExampleOperationExtenderActionV2(BiConsumer<Example, ExampleReference> customizer) {
        this.customizer = customizer;
    }

    public void extendWith(final Operation operation) {
        String operationId = operation.getOperationId();
        List<ExampleReference> references = exampleReferenceContext.getReference(new ExampleReferenceKey(operationId, ExampleReferenceType.RESPONSE));
        if (references == null || references.isEmpty()) {
            return;
        }
        extendWith(operation, references);
    }

    public void extendWith(Operation operation, List<ExampleReference> references) {
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
