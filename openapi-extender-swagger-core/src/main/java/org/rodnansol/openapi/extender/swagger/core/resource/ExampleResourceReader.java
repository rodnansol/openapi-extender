package org.rodnansol.openapi.extender.swagger.core.resource;

import org.rodnansol.openapi.extender.swagger.core.example.ExampleReferenceType;

/**
 * Interface describes what a resource reader implementation should have.
 * <p>
 * Readers must be able to resolve examples from different sources.
 * <p>
 * Check the default implementation that creates examples by reading files with specific file names: {@link FileNameBasedExampleResourceReader}
 */
public interface ExampleResourceReader {

    /**
     * Reads and creates example references based on the incoming type and resource.
     *
     * @return creation result or null if the resource can not be processed.
     * @throws ExampleResourceReaderException if the resource reading was not successful.
     */
    ExampleReferenceCreationResult createExampleReferences(ExampleReferenceType type, ExtenderResource resource);

}
