package org.rodnansol.core.operation;


import io.swagger.v3.oas.models.Operation;

/**
 *
 */
public interface OperationExtenderAction {

    /**
     * @param file
     * @return
     */
    void extendWith(Operation operation);

}
