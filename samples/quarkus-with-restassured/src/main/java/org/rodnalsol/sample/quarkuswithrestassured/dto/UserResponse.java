package org.rodnalsol.sample.quarkuswithrestassured.dto;

/**
 * Example DTO for representing user response.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
public class UserResponse {

    /**
     * Username.
     */
    private String username;

    /**
     * Full name.
     */
    private String fullName;

    public UserResponse() {
    }

    public UserResponse(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
