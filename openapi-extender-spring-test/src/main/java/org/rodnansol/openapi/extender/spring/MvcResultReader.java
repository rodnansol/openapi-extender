package org.rodnansol.openapi.extender.spring;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * Class dealing with operations based on the {@link MvcResult} interface instances.
 */
class MvcResultReader {

    private MvcResultReader() {
    }

    /**
     * Calculates the final operation name based on the incoming parameters.
     * <p>
     * If the operation parameter is given and not null it will be returned.
     * <p>
     * If not and the MvcResult is given, then it will try to read the {@link Operation} annotation's {@link Operation#operationId()} attribute, if it is null it will return the name of the handler method.
     *
     * @param operation operation's name.
     * @param result    {@link MvcResult} instance.
     * @return final operation name.
     */
    public static String getOperationName(final String operation, final MvcResult result) {
        String finalOperation = null;
        if (operation != null) {
            finalOperation = operation;
        } else {
            if (result.getHandler() instanceof HandlerMethod) {
                HandlerMethod handler = (HandlerMethod) result.getHandler();
                if (handler != null && handler.getMethod() != null) {
                    Operation operationAnnotation = handler.getMethodAnnotation(Operation.class);
                    finalOperation = operationAnnotation != null && StringUtils.hasText(operationAnnotation.operationId()) ? operationAnnotation.operationId() : handler.getMethod().getName();
                }
            }
        }
        return finalOperation;
    }

}
