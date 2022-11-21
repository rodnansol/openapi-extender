package org.rodnansol.openapi.extender.swagger.core.resource;

/**
 * Exception to be thrown when the resource can not be read.
 */
public class ExampleResourceReaderException extends RuntimeException {

    public ExampleResourceReaderException(String message) {
        super(message);
    }

    public ExampleResourceReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
