package org.rodnansol.core.operation;


import io.swagger.v3.oas.models.Operation;

/**
 * Operation extender interface that collects all available action calls.
 */
public interface OperationExtenderAction {

    /**
     * Extends the operation in a custom way.
     */
    void extendWith(Operation operation);

}
