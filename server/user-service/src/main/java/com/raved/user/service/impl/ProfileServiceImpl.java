package com.raved.user.service.impl;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.exception.UserNotFoundException;
import com.raved.user.mapper.UserMapper;
import com.raved.user.model.User;
import com.raved.user.repository.UserRepository;
import com.raved.user.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of ProfileService
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        logger.debug("Getting profile for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        return userMapper.toUserResponse(userOpt.get());
    }

    @Override
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        logger.info("Updating profile for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        
        // Update user fields using mapper
        userMapper.updateUserFromRequest(user, request);
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        logger.info("Profile updated successfully for user ID: {}", userId);
        
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse uploadProfilePicture(Long userId, MultipartFile file) {
        logger.info("Uploading profile picture for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        
        // TODO: Implement file upload logic
        // For now, we'll just set a placeholder URL
        String profilePictureUrl = "/uploads/profiles/" + userId + "/" + file.getOriginalFilename();
        
        user.setProfilePictureUrl(profilePictureUrl);
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        logger.info("Profile picture uploaded successfully for user ID: {}", userId);
        
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse deleteProfilePicture(Long userId) {
        logger.info("Deleting profile picture for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        user.setProfilePictureUrl(null);
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        logger.info("Profile picture deleted successfully for user ID: {}", userId);
        
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void verifyStudentId(Long userId, String studentId, MultipartFile idDocument) {
        logger.info("Verifying student ID for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        
        // TODO: Implement student ID verification logic
        // This would typically involve:
        // 1. Uploading the ID document
        // 2. OCR processing to extract student ID
        // 3. Verification against university database
        // 4. Manual review if needed
        
        user.setStudentId(studentId);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        logger.info("Student ID verification initiated for user ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getProfileCompletionPercentage(Long userId) {
        logger.debug("Calculating profile completion for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        int completedFields = 0;
        int totalFields = 10; // Total number of profile fields
        
        // Check required fields
        if (user.getFirstName() != null && !user.getFirstName().trim().isEmpty()) completedFields++;
        if (user.getLastName() != null && !user.getLastName().trim().isEmpty()) completedFields++;
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) completedFields++;
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) completedFields++;
        
        // Check optional fields
        if (user.getBio() != null && !user.getBio().trim().isEmpty()) completedFields++;
        if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().trim().isEmpty()) completedFields++;
        if (user.getUniversityId() != null) completedFields++;
        if (user.getFacultyId() != null) completedFields++;
        if (user.getDepartmentId() != null) completedFields++;
        if (user.getStudentId() != null && !user.getStudentId().trim().isEmpty()) completedFields++;
        
        int percentage = (completedFields * 100) / totalFields;
        logger.debug("Profile completion for user ID {}: {}%", userId, percentage);
        
        return percentage;
    }
}
