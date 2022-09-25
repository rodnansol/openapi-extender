package org.rodnansol.core.operation;

/**
 * Exception being thrown on errors in the extender actions.
 */
public class ExtenderActionException extends RuntimeException{

    public ExtenderActionException(String message) {
        super(message);
    }

    public ExtenderActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
