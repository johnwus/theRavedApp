package com.raved.user.controller;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for user profile operations
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * Get user profile by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    /**
     * Update user profile
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfile(userId, request));
    }

    /**
     * Upload profile picture
     */
    @PostMapping(value = "/{userId}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileService.uploadProfilePicture(userId, file));
    }

    /**
     * Delete profile picture
     */
    @DeleteMapping("/{userId}/picture")
    public ResponseEntity<UserResponse> deleteProfilePicture(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.deleteProfilePicture(userId));
    }

    /**
     * Verify student ID
     */
    @PostMapping(value = "/{userId}/verify-student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> verifyStudentId(
            @PathVariable Long userId,
            @RequestPart("studentId") String studentId,
            @RequestPart(value = "document", required = false) MultipartFile idDocument
    ) {
        profileService.verifyStudentId(userId, studentId, idDocument);
        return ResponseEntity.ok().build();
    }

    /**
     * Get profile completion percentage
     */
    @GetMapping("/{userId}/completion")
    public ResponseEntity<Integer> getCompletion(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileCompletionPercentage(userId));
    }
}
