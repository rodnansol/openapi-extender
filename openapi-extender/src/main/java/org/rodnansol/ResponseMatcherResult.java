package org.rodnansol;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;

import static org.rodnansol.IOUtils.copy;
import static org.rodnansol.IOUtils.readFileContent;

class ResponseMatcherResult {

    private final String statusCode;
    private final String description;
    private final String extension;
    private final String exampleContent;

    public ResponseMatcherResult(String statusCode, String description, String extension, File file) throws IOException {
        this.statusCode = statusCode;
        this.description = description;
        this.extension = extension;
        this.exampleContent = getContent(file);
    }

    public ResponseMatcherResult(Matcher matcher, File file) throws IOException {
        this(matcher.group(1), matcher.group(2), matcher.group(3), file);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public String getExampleContent() {
        return exampleContent;
    }

    private String getContent(File file) throws IOException {
        return readFileContent(file);
    }

}
