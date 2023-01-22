package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Operation customizer class that searches for different resources on the classpath and injects them into the {@link Operation}'s summary and/or description.
 * <p>
 * Set the {@link OperationDescriptionCustomizer#inBackground} to <b>true</b> with {@link OperationDescriptionCustomizer#setInBackground(boolean)} in case the operations should be enhanced in the background on different threads to not block the execution.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class OperationDescriptionCustomizer implements OperationCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationDescriptionCustomizer.class);
    private final OperationDescriptionConfiguration configuration;
    private final OperationDescriptionLoaderService operationDescriptionLoaderService;
    private final ExecutorService executorService;

    private boolean inBackground;
    private List<String> operationIdWhiteList;
    private List<String> operationIdBlackList;

    public OperationDescriptionCustomizer(OperationDescriptionConfiguration configuration,
                                          OperationDescriptionLoaderService operationDescriptionLoaderService) {
        this(configuration, operationDescriptionLoaderService, Executors.newFixedThreadPool(10));
    }

    public OperationDescriptionCustomizer(OperationDescriptionConfiguration configuration, OperationDescriptionLoaderService operationDescriptionLoaderService, ExecutorService executorService) {
        this.configuration = configuration;
        this.operationDescriptionLoaderService = operationDescriptionLoaderService;
        this.executorService = executorService;
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        Assert.notNull(operation, "operation is NULL");
        String operationId = operation.getOperationId();
        LOGGER.debug("Customizing Operation with id:[{}]", operationId);
        if (isBlacklisted(operationId)) {
            LOGGER.debug("Operation:[{}] is blacklisted, skipping enhancements...", operationId);
            return operation;
        }
        if (isNullWhitelistOrHasWhiteListEntry(operationId)) {
            LOGGER.debug("Operation:[{}] is whitelisted (or at least not blacklisted), executing enhancement", operationId);
            executeInjections(operation, handlerMethod);
        }
        return operation;
    }

    private boolean isNullWhitelistOrHasWhiteListEntry(String operationId) {
        return operationIdWhiteList == null || operationIdWhiteList.contains(operationId);
    }

    private boolean isBlacklisted(String operationId) {
        return operationIdBlackList != null && operationIdBlackList.contains(operationId);
    }

    private void executeInjections(Operation operation, HandlerMethod handlerMethod) {
        if (inBackground) {
            LOGGER.debug("Executing description customization operation for operationId:[{}] in the background, if resources exists they will be loaded later", operation.getOperationId());
            executorService.execute(() -> runInjections(operation, handlerMethod));
        } else {
            runInjections(operation, handlerMethod);
        }
    }

    private void runInjections(Operation operation, HandlerMethod handlerMethod) {
        String resourcesBasePath = configuration.getResourcesBasePath();
        String extension = configuration.getExtension();
        operationDescriptionLoaderService.injectSummary(resourcesBasePath, extension, operation, handlerMethod);
        operationDescriptionLoaderService.injectDescriptions(resourcesBasePath, extension, operation, handlerMethod);
    }

    public void setInBackground(boolean inBackground) {
        this.inBackground = inBackground;
    }

    public void setOperationIdWhiteList(List<String> operationIdWhiteList) {
        this.operationIdWhiteList = operationIdWhiteList;
    }

    public void setOperationIdBlackList(List<String> operationIdBlackList) {
        this.operationIdBlackList = operationIdBlackList;
    }
}
