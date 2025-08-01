package com.raved.user.service.impl;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse login(LoginRequest request) {
        // Implementation
        return null;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Implementation
        return null;
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // Implementation
        return null;
    }

    @Override
    public void logout(String accessToken) {
        // Implementation
    }

    @Override
    public void verifyEmail(String token) {
        // Implementation
    }

    @Override
    public void forgotPassword(String email) {
        // Implementation
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Implementation
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        // Implementation
    }
}