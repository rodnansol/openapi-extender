package org.rodnansol.core.example;

import java.util.Objects;

/**
 *
 */
public class ExampleReferenceKey {

    private final String operationId;
    private final ExampleReferenceType type;

    public ExampleReferenceKey(String operationId, ExampleReferenceType type) {
        this.operationId = operationId;
        this.type = type;
    }

    public static ExampleReferenceKey forRequest(String operationId) {
        return new ExampleReferenceKey(operationId, ExampleReferenceType.REQUEST);
    }

    public static ExampleReferenceKey forResponse(String operationId) {
        return new ExampleReferenceKey(operationId, ExampleReferenceType.RESPONSE);
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
