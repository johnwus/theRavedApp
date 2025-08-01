package com.raved.user.service;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Authenticate user and return JWT tokens
     */
    AuthResponse login(LoginRequest request);

    /**
     * Register a new user
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Refresh access token using refresh token
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * Logout user (invalidate tokens)
     */
    void logout(String accessToken);

    /**
     * Verify email with token
     */
    void verifyEmail(String token);

    /**
     * Send password reset email
     */
    void forgotPassword(String email);

    /**
     * Reset password with token
     */
    void resetPassword(String token, String newPassword);

    /**
     * Change password for authenticated user
     */
    void changePassword(Long userId, String currentPassword, String newPassword);
}
