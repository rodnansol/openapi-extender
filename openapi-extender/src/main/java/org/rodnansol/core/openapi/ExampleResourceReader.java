package org.rodnansol.core.openapi;

public interface ExampleResourceReader {

    /**
     *
     * @return
     */
    ExampleReference createExampleReference(ExampleReferenceType type, ExtenderResource file);

}
