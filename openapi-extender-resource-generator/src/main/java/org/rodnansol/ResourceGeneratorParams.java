package org.rodnansol;

/**
 *
 */
public interface ResourceGeneratorParams {


    String getOperation();

    String getDescription();

    int getStatus();

    String getContentType();

    byte[] getContent();
}
