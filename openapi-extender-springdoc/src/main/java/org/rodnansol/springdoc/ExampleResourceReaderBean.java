package org.rodnansol.springdoc;

import org.rodnansol.core.openapi.ExampleReferencePopulationAction;
import org.rodnansol.core.openapi.ExampleReferenceType;
import org.rodnansol.core.openapi.ExtenderResource;
import org.rodnansol.core.openapi.FileNameBasedExampleResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExampleResourceReaderBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleResourceReaderBean.class);

    private final String requestExamplePath;
    private final String responseExamplePath;

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final ExampleReferencePopulationAction exampleReferencePopulationAction;

    public ExampleResourceReaderBean() {
        this("classpath*:openapi-extender/requests/**", "classpath*:openapi-extender/responses/**", new ExampleReferencePopulationAction(new FileNameBasedExampleResourceReader()));
    }

    public ExampleResourceReaderBean(String requestExamplePath, String responseExamplePath, ExampleReferencePopulationAction exampleReferencePopulationAction) {
        this.requestExamplePath = requestExamplePath;
        this.responseExamplePath = responseExamplePath;
        this.exampleReferencePopulationAction = exampleReferencePopulationAction;
        initializeResources();
    }

    public void initializeResources() {
        try {
            processResources(requestExamplePath, ExampleReferenceType.REQUEST);
            processResources(responseExamplePath, ExampleReferenceType.RESPONSE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processResources(String path, ExampleReferenceType type) throws IOException {
        Resource[] resources = resolver.getResources(path);
        List<ExtenderResource> files = Arrays.stream(resources).map(resource -> {
                try {
                    return new ExtenderResource(resource.getFilename(), resource.getInputStream());
                } catch (IOException e) {
                    LOGGER.warn("File does not exist:[{}]", resource.getFilename());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        exampleReferencePopulationAction.populateExampleReferenceContext(type, files);
    }

}

