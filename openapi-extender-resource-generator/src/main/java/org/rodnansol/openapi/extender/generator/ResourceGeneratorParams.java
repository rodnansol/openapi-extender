package org.rodnansol.openapi.extender.generator;

/**
 * Interface decribing the input of the resource
 */
public interface ResourceGeneratorParams {

    /**
     * Returns the operation.
     */
    String getOperation();

    /**
     * Name/key of the example.
     */
    String getName();

    /**
     * Returns the optional description
     */
    String getDescription();

    /**
     * Returns the HTTP status code.
     */
    int getStatus();

    /**
     * Returns the content type.
     */
    String getContentType();

    /**
     * Returns the content in a byte array.
     */
    byte[] getContent();
}
