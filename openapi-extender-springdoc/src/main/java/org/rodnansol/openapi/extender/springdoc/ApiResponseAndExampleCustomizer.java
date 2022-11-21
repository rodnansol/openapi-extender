package org.rodnansol.openapi.extender.springdoc;

import io.swagger.v3.oas.models.Operation;
import org.rodnansol.openapi.extender.swagger.core.operation.OperationExtenderAction;
import org.rodnansol.openapi.extender.swagger.core.operation.RequestBodyExampleOperationExtenderAction;
import org.rodnansol.openapi.extender.swagger.core.operation.ResponseExampleOperationExtenderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that customizes incoming operations.
 */
public class ApiResponseAndExampleCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseAndExampleCustomizer.class);
    private final List<OperationExtenderAction> handlers;

    public ApiResponseAndExampleCustomizer() {
        this(new RequestBodyExampleOperationExtenderAction(), new ResponseExampleOperationExtenderAction());
    }

    public ApiResponseAndExampleCustomizer(OperationExtenderAction... actions) {
        Assert.notNull(actions, "Actions must not be null");
        this.handlers = new ArrayList<>(actions.length);
        Collections.addAll(handlers, actions);
    }


    /**
     * Extends the operation with the examples from the context.
     *
     * @param operation     input operation.
     * @param handlerMethod original handler method
     * @return extended operation.
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String operationId = operation.getOperationId();
        LOGGER.debug("Extending operation with ID:[{}]", operationId);
        handlers.forEach(operationExtenderAction -> executeAction(operation, operationExtenderAction));
        return operation;
    }

    private void executeAction(Operation operation, OperationExtenderAction operationExtenderAction) {
        try {
            operationExtenderAction.extendWith(operation);
        } catch (RuntimeException e) {
            LOGGER.error("Error during populating OpenAPI operation:[" + operation.getOperationId() + "] with extra elements", e);
        }
    }

}
