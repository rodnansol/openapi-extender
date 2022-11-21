package org.rodnansol.openapi.extender.swagger.core.resource;

import java.io.InputStream;

/**
 * Class that holds an InputStream with an associated name that can be a file name or other.
 */
public class ExtenderResource {

    private final String name;
    private final InputStream content;

    public ExtenderResource(String name, InputStream content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public InputStream getContent() {
        return content;
    }
}
