package org.rodnansol.openapi.extender.spring;

/**
 * Exception to be thrown from the ResultHandlers.
 */
public class OpenApiExtenderResultHandlerException extends RuntimeException {

    public OpenApiExtenderResultHandlerException(String message) {
        super(message);
    }

    public OpenApiExtenderResultHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
