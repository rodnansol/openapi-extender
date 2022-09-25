package org.rodnansol.generator;

/**
 * Exception to be thrown when the content type if unknown.
 */
public class UnknownContentTypeException extends RuntimeException {

    public UnknownContentTypeException(String message) {
        super(message);
    }

    public UnknownContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
