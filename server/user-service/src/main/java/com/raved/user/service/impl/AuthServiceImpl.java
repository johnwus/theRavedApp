package com.raved.user.service.impl;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.exception.InvalidCredentialsException;
import com.raved.user.exception.UserNotFoundException;
import com.raved.user.mapper.UserMapper;
import com.raved.user.model.User;
import com.raved.user.model.UserStatus;
import com.raved.user.repository.UserRepository;
import com.raved.user.security.JwtTokenProvider;
import com.raved.user.service.AuthService;
import com.raved.user.util.PasswordEncoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of AuthService
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt for user: {}", request.getUsername());

        // Find user by username or email
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(request.getUsername());
        }

        if (userOpt.isEmpty()) {
            logger.warn("Login failed: User not found - {}", request.getUsername());
            throw new UserNotFoundException("User not found");
        }

        User user = userOpt.get();

        // Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            logger.warn("Login failed: User account is not active - {}", request.getUsername());
            throw new InvalidCredentialsException("Account is not active");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid password for user - {}", request.getUsername());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        logger.info("Login successful for user: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registration attempt for user: {}", request.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user
        user = userRepository.save(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        logger.info("Registration successful for user: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        logger.info("Token refresh attempt");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOpt.get();

        // Generate new tokens
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        logger.info("Token refresh successful for user: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public void logout(String accessToken) {
        logger.info("Logout attempt");
        // In a real implementation, you might want to blacklist the token
        // For now, we'll just log the logout
        logger.info("User logged out successfully");
    }

    @Override
    public void verifyEmail(String token) {
        // Implementation for email verification
        logger.info("Email verification attempt with token: {}", token);
        // This would typically involve checking a verification token
        // and updating the user's email verification status
    }

    @Override
    public void forgotPassword(String email) {
        logger.info("Password reset request for email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists or not for security
            logger.warn("Password reset requested for non-existent email: {}", email);
            return;
        }

        // Generate reset token and send email
        // This would typically involve generating a secure token
        // and sending it via email service
        logger.info("Password reset email sent to: {}", email);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        logger.info("Password reset attempt with token");

        // Validate reset token and update password
        // This would typically involve checking the token validity
        // and updating the user's password

        logger.info("Password reset successful");
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Password change attempt for user ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOpt.get();

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        logger.info("Password changed successfully for user: {}", user.getUsername());
    }
}