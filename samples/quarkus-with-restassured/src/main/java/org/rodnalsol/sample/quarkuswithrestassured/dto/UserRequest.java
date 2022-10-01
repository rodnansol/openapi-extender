package org.rodnalsol.sample.quarkuswithrestassured.dto;

/**
 * Example DTO for representing user request.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class UserRequest {

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    private String password;

    /**
     * Password confirmation.
     */
    private String passwordConfirmation;

    /**
     * Full name.
     */
    private String fullName;

    public UserRequest() {
    }

    public UserRequest(String username, String password, String passwordConfirmation, String fullName) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
