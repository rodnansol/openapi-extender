package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Class that initiates the injection to the {@link Operation} class based on different resource laoding strategies.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class OperationDescriptionLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationDescriptionLoaderService.class);
    private final Charset resourceCharset;
    private final List<ResourceLoader> descriptionResourceLoaders;
    private final List<ResourceLoader> summaryResourceLoaders;


    public OperationDescriptionLoaderService() {
        this(StandardCharsets.UTF_8, ResourceLoaderFactory.getDescriptionResourceLoaders(), ResourceLoaderFactory.getSummaryResourceLoaders());

    }

    public OperationDescriptionLoaderService(Charset resourceCharset) {
        this(resourceCharset, ResourceLoaderFactory.getDescriptionResourceLoaders(), ResourceLoaderFactory.getSummaryResourceLoaders());

    }

    public OperationDescriptionLoaderService(Charset resourceCharset, List<ResourceLoader> descriptionResourceLoaders, List<ResourceLoader> summaryResourceLoaders) {
        this.resourceCharset = resourceCharset;
        this.descriptionResourceLoaders = Objects.requireNonNull(descriptionResourceLoaders, "descriptionResourceLoaders is NULL");
        this.summaryResourceLoaders = Objects.requireNonNull(summaryResourceLoaders, "summaryResourceLoaders is NULL");
    }

    /**
     * Seeks and injects resource content for the given operation's description if the resources ar available.
     *
     * @param resourceBasePath resources base path.
     * @param extension        resource extension.
     * @param operation        operation to examine and enhance.
     * @param handlerMethod    operation's Spring based handler method.
     */
    void injectDescriptions(String resourceBasePath, String extension, Operation operation, HandlerMethod handlerMethod) {
        Assert.notNull(operation, "operation is NULL");
        Assert.notNull(handlerMethod, "handlerMethod is NULL");
        String operationId = operation.getOperationId();
        if (StringUtils.isBlank(operation.getDescription())) {
            LOGGER.debug("Populating description for operation with id:[{}]", operationId);
            for (ResourceLoader resourceLoader : descriptionResourceLoaders) {
                LOGGER.trace("Running [{}] to find 'description' resource for [{}]", resourceLoader, operationId);
                byte[] bytes = resourceLoader.loadResource(new LoadResourceCommand(resourceBasePath, extension, operation, handlerMethod));
                if (bytes != null && bytes.length > 0) {
                    LOGGER.trace("[{}] found resources, operation's description with id:[{}] is being overwritten", resourceLoader, operationId);
                    operation.setDescription(new String(bytes, resourceCharset));
                    break;
                } else {
                    LOGGER.debug("Resource loader:[{}] is unable to resolve the description for the operation with id:[{}]", resourceLoader.getClass().getName(), operationId);
                }
            }
        } else {
            LOGGER.debug("Description for operation with id:[{}] is already set", operationId);
        }
    }

    /**
     * Seeks and injects resource content for the given operation's description if the resources ar available.
     *
     * @param resourceBasePath resources base path.
     * @param extension        resource extension.
     * @param operation        operation to examine and enhance.
     * @param handlerMethod    operation's Spring based handler method.
     */
    void injectSummary(String resourceBasePath, String extension, Operation operation, HandlerMethod handlerMethod) {
        Assert.notNull(operation, "operation is NULL");
        Assert.notNull(handlerMethod, "handlerMethod is NULL");
        String operationId = operation.getOperationId();
        if (StringUtils.isBlank(operation.getSummary())) {
            LOGGER.debug("Populating summary for operation with id:[{}]", operationId);
            for (ResourceLoader resourceLoader : summaryResourceLoaders) {
                LOGGER.trace("Running [{}] to find 'summary' resource for [{}]", resourceLoader, operationId);
                byte[] bytes = resourceLoader.loadResource(new LoadResourceCommand(resourceBasePath, extension, operation, handlerMethod));
                if (bytes != null && bytes.length > 0) {
                    LOGGER.trace("[{}] found resources, operation's summary with id:[{}] is being overwritten", resourceLoader, operationId);
                    operation.setSummary(new String(bytes, resourceCharset));
                    break;
                } else {
                    LOGGER.debug("Resource loader:[{}] is unable to resolve the summary for the operation with id:[{}]", resourceLoader.getClass().getName(), operationId);
                }
            }
        } else {
            LOGGER.debug("Summary for operation with id:[{}] is already set", operationId);
        }
    }


}
