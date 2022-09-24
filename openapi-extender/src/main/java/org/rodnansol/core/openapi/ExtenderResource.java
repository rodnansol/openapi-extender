package org.rodnansol.core.openapi;

import java.io.InputStream;

public class ExtenderResource {

    private final String fileName;
    private final InputStream content;

    public ExtenderResource(String fileName, InputStream content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getContent() {
        return content;
    }
}
