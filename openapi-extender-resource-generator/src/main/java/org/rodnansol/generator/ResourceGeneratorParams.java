package org.rodnansol.generator;

/**
 * Interface decribing the input of the resource 
 */
public interface ResourceGeneratorParams {

    String getOperation();

    String getName();

    String getDescription();

    int getStatus();

    String getContentType();

    byte[] getContent();
}
