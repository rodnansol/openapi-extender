package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

public class OperationDescriptionCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationDescriptionCustomizer.class);

    private final String resourcesBasePath;
    private final String extension;
    private final OperationDescriptionLoaderService operationDescriptionLoaderService;

    public OperationDescriptionCustomizer(String resourcesBasePath,
                                          String extension,
                                          OperationDescriptionLoaderService operationDescriptionLoaderService) {
        this.resourcesBasePath = resourcesBasePath;
        this.extension = extension;
        this.operationDescriptionLoaderService = operationDescriptionLoaderService;
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        operationDescriptionLoaderService.injectSummaries(resourcesBasePath, extension, operation, handlerMethod);
        operationDescriptionLoaderService.injectDescriptions(resourcesBasePath, extension, operation, handlerMethod);
        return operation;
    }
}
