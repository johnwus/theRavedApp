package com.raved.user.service;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProfileService for TheRavedApp
 */
public interface ProfileService {

    /**
     * Get user profile by ID
     */
    UserResponse getProfile(Long userId);

    /**
     * Update user profile
     */
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    /**
     * Upload profile picture
     */
    UserResponse uploadProfilePicture(Long userId, MultipartFile file);

    /**
     * Delete profile picture
     */
    UserResponse deleteProfilePicture(Long userId);

    /**
     * Verify student ID
     */
    void verifyStudentId(Long userId, String studentId, MultipartFile idDocument);

    /**
     * Get profile completion percentage
     */
    int getProfileCompletionPercentage(Long userId);
}
