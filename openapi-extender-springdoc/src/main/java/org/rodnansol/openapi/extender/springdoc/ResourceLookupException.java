package org.rodnansol.openapi.extender.springdoc;

/**
 * Exception to be thrown at when a resource could not be loaded.
 *
 * @author nandorholozsnyak
 * @since 0.2.0
 */
public class ResourceLookupException extends RuntimeException {

    public ResourceLookupException(String message) {
        super(message);
    }

    public ResourceLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
