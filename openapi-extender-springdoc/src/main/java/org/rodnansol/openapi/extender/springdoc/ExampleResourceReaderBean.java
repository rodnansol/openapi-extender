package org.rodnansol.openapi.extender.springdoc;

import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferencePopulationAction;
import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceType;
import org.rodnansol.openapi.extender.swagger.core.resource.ExtenderResource;
import org.rodnansol.openapi.extender.swagger.core.resource.FileNameBasedExampleResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Resource reader bean.
 */
public class ExampleResourceReaderBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleResourceReaderBean.class);
    private static final String DEFAULT_REQUESTS_PATH = "classpath*:openapi-extender/requests/**";
    private static final String DEFAULT_RESPONSES_PATH = "classpath*:openapi-extender/responses/**";

    private final List<String> requestExamplePaths;
    private final List<String> responseExamplePaths;

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final ExampleReferencePopulationAction exampleReferencePopulationAction;

    public ExampleResourceReaderBean() {
        this(Collections.singletonList(DEFAULT_REQUESTS_PATH),
            Collections.singletonList(DEFAULT_RESPONSES_PATH),
            new ExampleReferencePopulationAction(new FileNameBasedExampleResourceReader()));
    }

    public ExampleResourceReaderBean(List<String> requestExamplePaths,
                                     List<String> responseExamplePaths,
                                     ExampleReferencePopulationAction exampleReferencePopulationAction) {
        this.requestExamplePaths = requestExamplePaths;
        this.responseExamplePaths = responseExamplePaths;
        this.exampleReferencePopulationAction = exampleReferencePopulationAction;
    }

    /**
     * Initializes the resources and stores them in the context.
     */
    public void initializeResources() {
        try {
            processResources(requestExamplePaths, ExampleReferenceType.REQUEST);
        } catch (IOException e) {
            LOGGER.error("Error during processing requests", e);
        }
        try {
            processResources(responseExamplePaths, ExampleReferenceType.RESPONSE);
        } catch (IOException e) {
            LOGGER.error("Error during processing responses", e);
        }
    }

    private void processResources(List<String> paths, ExampleReferenceType type) throws IOException {
        LOGGER.debug("Processing resources on the given paths:[{}] with type:[{}]", paths, type);
        for (String path : paths) {
            processResources(type, path);
        }
    }

    private void processResources(ExampleReferenceType type, String path) throws IOException {
        Resource[] resources = resolver.getResources(path);
        List<ExtenderResource> files = Arrays.stream(resources).map(this::createExtenderResource)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        exampleReferencePopulationAction.populateExampleReferenceContext(type, files);
    }

    private ExtenderResource createExtenderResource(Resource resource) {
        try {
            return new ExtenderResource(resource.getFilename(), resource.getInputStream());
        } catch (IOException e) {
            LOGGER.warn("File does not exist:[{}]", resource.getFilename());
            return null;
        }
    }

}

