package org.rodnansol.core.operation;


import io.swagger.v3.oas.models.Operation;

/**
 *
 */
public interface OperationExtenderActionV2 {

    /**
     * @param file
     * @return
     */
    void extendWith(Operation operation);

}
