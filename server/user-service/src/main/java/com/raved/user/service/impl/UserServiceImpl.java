package com.raved.user.service.impl;

import com.raved.user.dto.response.UserResponse;
import com.raved.user.exception.UserNotFoundException;
import com.raved.user.mapper.UserMapper;
import com.raved.user.model.User;
import com.raved.user.model.UserStatus;
import com.raved.user.repository.UserRepository;
import com.raved.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        logger.debug("Getting user by ID: {}", id);
        
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByUsername(String username) {
        logger.debug("Getting user by username: {}", username);
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        logger.debug("Getting user by email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        logger.debug("Getting all users with pagination: {}", pageable);
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        logger.debug("Searching users with term: {}", searchTerm);
        
        Page<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm, searchTerm, pageable);
        return users.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByStatus(UserStatus status) {
        logger.debug("Getting users by status: {}", status);
        
        List<User> users = userRepository.findByStatus(status);
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUserStatus(Long id, UserStatus status) {
        logger.info("Updating user status for ID: {} to {}", id, status);
        
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        
        User user = userOpt.get();
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        logger.info("User status updated successfully for ID: {}", id);
        
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        logger.debug("Checking if username exists: {}", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        logger.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateLastLogin(Long userId) {
        logger.debug("Updating last login for user ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            logger.debug("Last login updated for user ID: {}", userId);
        } else {
            logger.warn("User not found for last login update: {}", userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        logger.debug("Finding user entity by ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        logger.debug("Finding user entity by username or email: {}", usernameOrEmail);
        
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }
        return userOpt;
    }

    @Override
    public User saveUser(User user) {
        logger.debug("Saving user entity: {}", user.getUsername());
        
        user.setUpdatedAt(LocalDateTime.now());
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        
        User savedUser = userRepository.save(user);
        logger.debug("User entity saved successfully: {}", savedUser.getId());
        
        return savedUser;
    }
}
