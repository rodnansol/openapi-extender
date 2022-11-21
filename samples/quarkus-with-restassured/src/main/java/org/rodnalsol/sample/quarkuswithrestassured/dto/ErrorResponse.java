package org.rodnalsol.sample.quarkuswithrestassured.dto;

/**
 * Example DTO for representing error response.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class ErrorResponse {

    /**
     * Message.
     */
    private String message;

    /**
     * Cause.
     */
    private String cause;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
