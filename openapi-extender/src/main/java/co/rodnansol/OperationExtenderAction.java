package co.rodnansol;


import io.swagger.v3.oas.models.Operation;

import java.io.File;

/**
 *
 */
public interface OperationExtenderAction {

    /**
     * @param file
     * @return
     */
    void extendWith(Operation operation, File file);

    /**
     *
     * @return
     */
    String workingDirectory();

}
