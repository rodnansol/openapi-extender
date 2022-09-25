package org.rodnansol.core.example;

import org.rodnansol.core.resource.ExampleReferenceCreationResult;
import org.rodnansol.core.resource.ExampleResourceReader;
import org.rodnansol.core.resource.ExtenderResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class that uses an {@link ExampleResourceReader} instance and populates the {@link  ExampleReferenceContext} with the processed objects.
 */
public class ExampleReferencePopulationAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleReferencePopulationAction.class);
    private final ExampleReferenceContext exampleReferenceContext;
    private final ExampleResourceReader exampleResourceReader;

    public ExampleReferencePopulationAction(ExampleResourceReader exampleResourceReader) {
        this(ExampleReferenceContext.getInstance(), exampleResourceReader);
    }

    public ExampleReferencePopulationAction(ExampleReferenceContext exampleReferenceContext, ExampleResourceReader exampleResourceReader) {
        this.exampleReferenceContext = exampleReferenceContext;
        this.exampleResourceReader = exampleResourceReader;
    }


    /**
     * Processes the incoming resources and stores them in the context.
     *
     * @param type      type of the references.
     * @param resources resources to be processed.
     */
    public void populateExampleReferenceContext(ExampleReferenceType type, List<ExtenderResource> resources) {
        if (resources == null) {
            throw new IllegalArgumentException("resources are NULL");
        }
        resources.forEach(resource -> storeAsExample(type, resource));
    }

    private void storeAsExample(ExampleReferenceType type, ExtenderResource extenderResource) {
        ExampleReferenceCreationResult creationResult = exampleResourceReader.createExampleReferences(type, extenderResource);
        if (creationResult == null || creationResult.getReferences().isEmpty()) {
            return;
        }
        ExampleReferenceKey referenceKey = new ExampleReferenceKey(creationResult.getOperationId(), type);
        creationResult.getReferences().forEach(exampleReference -> exampleReferenceContext.addReference(referenceKey, exampleReference));
        LOGGER.debug("Resource with name:[{}] processed and stored in context", extenderResource.getName());
    }

}
