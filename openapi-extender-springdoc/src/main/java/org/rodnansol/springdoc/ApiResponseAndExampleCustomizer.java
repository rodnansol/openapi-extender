package org.rodnansol.springdoc;

import io.swagger.v3.oas.models.Operation;
import org.rodnansol.core.operation.OperationExtenderActionV2;
import org.rodnansol.core.operation.RequestBodyExampleOperationExtenderActionV2;
import org.rodnansol.core.operation.ResponseExampleOperationExtenderActionV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ApiResponseAndExampleCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseAndExampleCustomizer.class);
    private final List<OperationExtenderActionV2> handlers;

    public ApiResponseAndExampleCustomizer() {
        this(new RequestBodyExampleOperationExtenderActionV2(), new ResponseExampleOperationExtenderActionV2());
    }

    public ApiResponseAndExampleCustomizer(OperationExtenderActionV2... actions) {
        Assert.notNull(actions, "Actions must not be null");
        this.handlers = new ArrayList<>(actions.length);
        Collections.addAll(handlers, actions);
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
        handlers.forEach(operationExtenderAction -> executeAction(operation, operationExtenderAction));
        return operation;
    }

    private void executeAction(Operation operation, OperationExtenderActionV2 operationExtenderAction) {
        try {
            operationExtenderAction.extendWith(operation);
        } catch (RuntimeException e) {
            LOGGER.error("Error during populating OpenAPI operation:[" + operation.getOperationId() + "] with extra elements", e);
        }
    }

}
