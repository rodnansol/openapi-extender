package org.rodnalsol.openapi.extender.restassured.exception;

/**
 * Exception for OpenAPI filters.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class OpenApiFilterException extends RuntimeException {

    /**
     * Constructs a new {@link OpenApiFilterException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public OpenApiFilterException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@link OpenApiFilterException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public OpenApiFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
