package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
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
public final class RequestBodyExampleOperationExtenderActionV2 implements OperationExtenderActionV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyExampleOperationExtenderActionV2.class);
    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();
    private static final BiConsumer<Example, ExampleReference> DEFAULT_CUSTOMIZER = (example, exampleReference) -> {
        String desc = exampleReference.getDescription() + " - Returns: HTTP " + exampleReference.getStatusCode();
        example.set$ref(exampleReference.getReferredName());
        example.setDescription(desc);
    };
    private final BiConsumer<Example, ExampleReference> customizer;

    public RequestBodyExampleOperationExtenderActionV2() {
        this(DEFAULT_CUSTOMIZER);
    }

    public RequestBodyExampleOperationExtenderActionV2(BiConsumer<Example, ExampleReference> customizer) {
        this.customizer = customizer;
    }

    public void extendWith(Operation operation) {
        String operationId = operation.getOperationId();
        List<ExampleReference> references = exampleReferenceContext.getReference(new ExampleReferenceKey(operationId, ExampleReferenceType.REQUEST));
        if (references == null || references.isEmpty()) {
            return;
        }
        extendWith(operation, references);
    }

    public void extendWith(Operation operation, List<ExampleReference> references) {
        RequestBody requestBody = operation.getRequestBody() == null ? new RequestBody() : operation.getRequestBody();
        operation.setRequestBody(requestBody);
        Content content = requestBody.getContent() == null ? new Content() : requestBody.getContent();
        requestBody.setContent(content);
        references.forEach(exampleReference -> {
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
