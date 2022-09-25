package org.rodnansol.springdoc;

/**
 * Exception to be thrown at when a resource could not be loaded.
 */
public class ResourceLookupException extends RuntimeException {

    public ResourceLookupException(String message) {
        super(message);
    }

    public ResourceLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
