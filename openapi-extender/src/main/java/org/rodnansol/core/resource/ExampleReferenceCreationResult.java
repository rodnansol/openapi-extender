package org.rodnansol.core.resource;

import org.rodnansol.core.example.ExampleReference;

import java.util.List;

/**
 * Example reference creation result that stores the operation ID and the associated  example references.
 */
public class ExampleReferenceCreationResult {

    private final String operationId;
    private final List<ExampleReference> references;

    public ExampleReferenceCreationResult(String operationId, List<ExampleReference> references) {
        this.operationId = operationId;
        this.references = references;
    }

    public String getOperationId() {
        return operationId;
    }

    public List<ExampleReference> getReferences() {
        return references;
    }
}
