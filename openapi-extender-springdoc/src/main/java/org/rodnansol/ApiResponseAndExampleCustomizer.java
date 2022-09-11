package org.rodnansol;

import io.swagger.v3.oas.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ApiResponseAndExampleCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseAndExampleCustomizer.class);
    private static final List<OperationExtenderAction> HANDLERS = new LinkedList<>();

    public ApiResponseAndExampleCustomizer() {
        this("openapi-extender/requests/", "openapi-extender/responses/");
    }

    public ApiResponseAndExampleCustomizer(String requestExamplesPath, String responseExamplePath) {
        HANDLERS.add(new RequestBodyExampleOperationExtenderAction(requestExamplesPath));
        HANDLERS.add(new ResponseExampleOperationExtenderAction(responseExamplePath));
    }

    /**
     * @param operation     input operation
     * @param handlerMethod original handler method
     * @return
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String operationId = operation.getOperationId();
        LOGGER.debug("Extending operation with ID:[{}]", operationId);

        HANDLERS.forEach(operationExtenderAction -> {
            try {
                URL resource = getResourcesFolder(operationId, operationExtenderAction);
                if (resource == null) {
                    LOGGER.debug("No resource for operation:[{}]", operation);
                    return;
                }
                File folder = new File(resource.getFile());
                File[] files = folder.listFiles();
                if (files == null) {
                    return;
                }
                for (File file : files) {
                    operationExtenderAction.extendWith(operation, file);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Error during populating OpenAPI operation:[" + operationId + "] with extra elements", e);
            }
        });

        return operation;
    }

    private URL getResourcesFolder(String operationId, OperationExtenderAction operationExtenderAction) {
        return getClass().getClassLoader().getResource(operationExtenderAction.workingDirectory() + operationId);
    }

}
