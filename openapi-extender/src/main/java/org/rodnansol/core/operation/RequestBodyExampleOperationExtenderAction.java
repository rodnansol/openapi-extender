package org.rodnansol.core.operation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.rodnansol.core.example.ExampleReference;
import org.rodnansol.core.example.ExampleReferenceContext;
import org.rodnansol.core.example.ExampleReferenceKey;
import org.rodnansol.core.example.ExampleReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Action class that extends the {@link Operation}'s request body part with examples.
 */
public final class RequestBodyExampleOperationExtenderAction implements OperationExtenderAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyExampleOperationExtenderAction.class);
    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();
    private static final BiConsumer<Example, ExampleReference> DEFAULT_CUSTOMIZER = (example, exampleReference) -> {
        String desc;
        if (exampleReference.getDescription() != null && !exampleReference.getDescription().trim().equals("")) {
            desc = exampleReference.getDescription() + " - Returns: HTTP " + exampleReference.getStatusCode();
        } else {
            desc = "Returns: HTTP " + exampleReference.getStatusCode();
        }
        example.set$ref(exampleReference.getReferredName());
        example.setDescription(desc);
    };
    private final BiConsumer<Example, ExampleReference> customizer;

    public RequestBodyExampleOperationExtenderAction() {
        this(DEFAULT_CUSTOMIZER);
    }

    public RequestBodyExampleOperationExtenderAction(BiConsumer<Example, ExampleReference> customizer) {
        this.customizer = customizer;
    }

    /**
     * Extends the {@link Operation}'s request body part from the context.
     *
     * @param operation to be extended  from the context.
     */
    public void extendWith(Operation operation) {
        if(operation == null) {
            throw new IllegalArgumentException("operation is NULL");
        }
        String operationId = operation.getOperationId();
        LOGGER.debug("Extending operation's request body part with examples, operationId:[{}]", operationId);
        List<ExampleReference> references = exampleReferenceContext.getReference(new ExampleReferenceKey(operationId, ExampleReferenceType.REQUEST));
        if (references == null || references.isEmpty()) {
            return;
        }
        extendWith(operation, references);
    }

    private void extendWith(Operation operation, List<ExampleReference> references) {
        RequestBody requestBody = operation.getRequestBody() == null ? new RequestBody() : operation.getRequestBody();
        operation.setRequestBody(requestBody);
        Content content = requestBody.getContent() == null ? new Content() : requestBody.getContent();
        requestBody.setContent(content);
        references.forEach(exampleReference -> convertToExample(content, exampleReference));

    }

    private void convertToExample(Content content, ExampleReference exampleReference) {
        String rawMediaType = exampleReference.getMediaType();
        MediaType mediaType = content.getOrDefault(rawMediaType, new MediaType());
        if (!content.containsKey(rawMediaType)) {
            content.addMediaType(rawMediaType, mediaType);
        }
        Example example = new Example();
        customizer.accept(example, exampleReference);
        mediaType.addExamples(exampleReference.getName(), example);
    }


}
