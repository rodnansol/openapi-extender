package org.rodnansol.core.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExampleReferencePopulationAction {

    private final ExampleReferenceContext exampleReferenceContext = ExampleReferenceContext.getInstance();

    private final ExampleResourceReader exampleResourceReader;

    public ExampleReferencePopulationAction(ExampleResourceReader exampleResourceReader) {
        this.exampleResourceReader = exampleResourceReader;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleReferencePopulationAction.class);

    /**
     * @param openAPI
     * @param type
     * @param files
     */
    public void populateExampleReferenceContext(ExampleReferenceType type, List<ExtenderResource> files) {
        files.forEach(resource -> storeAsExample(type, resource));
    }

    private void storeAsExample(ExampleReferenceType type, ExtenderResource extenderResource) {
        final String fileName = extenderResource.getFileName();
        LOGGER.debug("Starting to read Open API example from file [{}]", fileName);
        ExampleReference exampleReference = exampleResourceReader.createExampleReference(type, extenderResource);
        if (exampleReference == null) {
            return;
        }
        ExampleReferenceKey referenceKey = new ExampleReferenceKey(exampleReference.getOperationId(), type);
        exampleReferenceContext.addReference(referenceKey, exampleReference);
        LOGGER.debug("Open API example has been read from file [{}]", fileName);
    }

}
