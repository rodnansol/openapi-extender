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
     * @param operation
     * @param handlerMethod
     */
    void injectDescriptions(String resourceBasePath, String extension, Operation operation, HandlerMethod handlerMethod) {
        Assert.notNull(operation, "operation is NULL");
        Assert.notNull(handlerMethod, "handlerMethod is NULL");
        String operationId = operation.getOperationId();
        LOGGER.info("Populating description for operation with id:[{}]", operationId);
        if (StringUtils.isBlank(operation.getDescription())) {
            for (ResourceLoader resourceLoader : descriptionResourceLoaders) {
                byte[] bytes = resourceLoader.loadResource(new LoadResourceCommand(resourceBasePath, extension, operation, handlerMethod));
                if (bytes != null && bytes.length > 0) {
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
     * @param resourceBasePath
     * @param extension
     * @param operation
     * @param handlerMethod
     */
    void injectSummaries(String resourceBasePath, String extension, Operation operation, HandlerMethod handlerMethod) {
        Assert.notNull(operation, "operation is NULL");
        Assert.notNull(handlerMethod, "handlerMethod is NULL");
        String operationId = operation.getOperationId();
        LOGGER.info("Populating summary for operation with id:[{}]", operationId);
        if (StringUtils.isBlank(operation.getSummary())) {
            for (ResourceLoader resourceLoader : summaryResourceLoaders) {
                byte[] bytes = resourceLoader.loadResource(new LoadResourceCommand(resourceBasePath, extension, operation, handlerMethod));
                if (bytes != null && bytes.length > 0) {
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
