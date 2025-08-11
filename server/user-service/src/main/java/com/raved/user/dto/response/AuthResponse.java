package com.raved.user.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for authentication response containing JWT tokens and user information
 * after successful authentication.
 */
public class AuthResponse {

    // Authentication tokens
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    
    // User information
    private UserResponse user;

    // Default constructor
    public AuthResponse() {}

    // Parameterized constructor
    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Builder pattern for fluent API
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing AuthResponse instances
     */
    public static class Builder {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserResponse user;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder user(UserResponse user) {
            this.user = user;
            return this;
        }

        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.accessToken = this.accessToken;
            response.refreshToken = this.refreshToken;
            response.tokenType = this.tokenType;
            response.expiresIn = this.expiresIn;
            response.user = this.user;
            return response;
        }
    }

    // Getters and Setters
    public String getAccessToken() { 
        return accessToken; 
    }
    
    public void setAccessToken(String accessToken) { 
        this.accessToken = accessToken; 
    }

    public String getRefreshToken() { 
        return refreshToken; 
    }
    
    public void setRefreshToken(String refreshToken) { 
        this.refreshToken = refreshToken; 
    }

    public String getTokenType() { 
        return tokenType; 
    }
    
    public void setTokenType(String tokenType) { 
        this.tokenType = tokenType; 
    }

    public Long getExpiresIn() { 
        return expiresIn; 
    }
    
    public void setExpiresIn(Long expiresIn) { 
        this.expiresIn = expiresIn; 
    }

    public UserResponse getUser() { 
        return user; 
    }
    
    public void setUser(UserResponse user) { 
        this.user = user; 
    }
}
