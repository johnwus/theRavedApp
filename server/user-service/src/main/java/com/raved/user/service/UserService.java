package com.raved.user.service;

import com.raved.user.dto.response.UserResponse;
import com.raved.user.model.User;
import com.raved.user.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations
 */
public interface UserService {

    /**
     * Get user by ID
     */
    Optional<UserResponse> getUserById(Long id);

    /**
     * Get user by username
     */
    Optional<UserResponse> getUserByUsername(String username);

    /**
     * Get user by email
     */
    Optional<UserResponse> getUserByEmail(String email);

    /**
     * Get all users with pagination
     */
    Page<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Search users
     */
    Page<UserResponse> searchUsers(String searchTerm, Pageable pageable);

    /**
     * Get users by status
     */
    List<UserResponse> getUsersByStatus(UserStatus status);

    /**
     * Update user status
     */
    UserResponse updateUserStatus(Long id, UserStatus status);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Update last login time
     */
    void updateLastLogin(Long userId);

    /**
     * Get user entity by ID (for internal use)
     */
    Optional<User> findUserById(Long id);

    /**
     * Get user entity by username or email (for internal use)
     */
    Optional<User> findUserByUsernameOrEmail(String usernameOrEmail);

    /**
     * Save user entity (for internal use)
     */
    User saveUser(User user);
}
