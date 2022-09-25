package org.rodnansol.core.example;

import java.util.Objects;

/**
 * Represents a key in the context.
 */
public class ExampleReferenceKey {

    private final String operationId;
    private final ExampleReferenceType type;

    public ExampleReferenceKey(String operationId, ExampleReferenceType type) {
        this.operationId = operationId;
        this.type = type;
    }

    public String getOperationId() {
        return operationId;
    }

    public ExampleReferenceType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleReferenceKey that = (ExampleReferenceKey) o;
        return Objects.equals(operationId, that.operationId) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, type);
    }
}
