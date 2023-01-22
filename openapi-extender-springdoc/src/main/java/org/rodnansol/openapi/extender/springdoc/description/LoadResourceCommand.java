package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

/**
 * Class storing all the necessary information to load a resource based on {@link Operation} or {@link HandlerMethod}
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class LoadResourceCommand {

    private final String resourceBasePath;
    private final String extension;
    private final Operation operation;
    private final HandlerMethod handlerMethod;

    public LoadResourceCommand(String resourceBasePath, String extension, Operation operation, HandlerMethod handlerMethod) {
        this.resourceBasePath = resourceBasePath;
        this.extension = extension;
        this.operation = operation;
        this.handlerMethod = handlerMethod;
    }

    public String getResourceBasePath() {
        return resourceBasePath;
    }

    public String getExtension() {
        return extension;
    }

    public Operation getOperation() {
        return operation;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }
}
