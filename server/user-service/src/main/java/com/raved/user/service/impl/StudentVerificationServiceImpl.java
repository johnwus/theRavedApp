package com.raved.user.service.impl;

import com.raved.user.dto.request.StudentVerificationRequest;
import com.raved.user.dto.response.StudentVerificationResponse;
import com.raved.user.event.UserEventPublisher;
import com.raved.user.exception.VerificationFailedException;
import com.raved.user.mapper.StudentVerificationMapper;
import com.raved.user.model.StudentVerification;
import com.raved.user.model.User;
import com.raved.user.repository.StudentVerificationRepository;
import com.raved.user.repository.UserRepository;
import com.raved.user.service.StudentVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of StudentVerificationService
 */
@Service
@Transactional
public class StudentVerificationServiceImpl implements StudentVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(StudentVerificationServiceImpl.class);

    @Autowired
    private StudentVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentVerificationMapper verificationMapper;

    @Autowired
    private UserEventPublisher eventPublisher;

    @Override
    public StudentVerificationResponse submitVerification(StudentVerificationRequest request) {
        logger.info("Submitting student verification for user: {}", request.getUserId());
        
        // Check if user exists
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new VerificationFailedException("User not found: " + request.getUserId());
        }
        
        User user = userOpt.get();
        
        // Check if verification already exists
        Optional<StudentVerification> existingVerification = 
                verificationRepository.findByUserIdAndStatus(request.getUserId(), StudentVerification.VerificationStatus.PENDING);
        
        if (existingVerification.isPresent()) {
            throw new VerificationFailedException("Verification already pending for user: " + request.getUserId());
        }
        
        // Create new verification
        StudentVerification verification = verificationMapper.toStudentVerification(request);
        verification.setVerificationToken(generateVerificationToken());
        verification.setStatus(StudentVerification.VerificationStatus.PENDING);
        verification.setSubmittedAt(LocalDateTime.now());
        verification.setCreatedAt(LocalDateTime.now());
        verification.setUpdatedAt(LocalDateTime.now());
        
        StudentVerification savedVerification = verificationRepository.save(verification);
        
        // Publish event
        eventPublisher.publishStudentVerificationSubmittedEvent(user.getId(), savedVerification.getId());
        
        logger.info("Student verification submitted: {}", savedVerification.getId());
        return verificationMapper.toStudentVerificationResponse(savedVerification);
    }

    @Override
    public StudentVerificationResponse approveVerification(Long verificationId, Long adminId) {
        logger.info("Approving student verification: {} by admin: {}", verificationId, adminId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findById(verificationId);
        if (verificationOpt.isEmpty()) {
            throw new VerificationFailedException("Verification not found: " + verificationId);
        }
        
        StudentVerification verification = verificationOpt.get();
        if (verification.getStatus() != StudentVerification.VerificationStatus.PENDING) {
            throw new VerificationFailedException("Verification is not pending: " + verificationId);
        }
        
        // Update verification status
        verification.setStatus(StudentVerification.VerificationStatus.APPROVED);
        verification.setReviewedBy(adminId);
        verification.setReviewedAt(LocalDateTime.now());
        verification.setUpdatedAt(LocalDateTime.now());
        
        StudentVerification savedVerification = verificationRepository.save(verification);
        
        // Update user status
        Optional<User> userOpt = userRepository.findById(verification.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsVerified(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Publish event
            eventPublisher.publishStudentVerificationApprovedEvent(user.getId(), verificationId);
        }
        
        logger.info("Student verification approved: {}", verificationId);
        return verificationMapper.toStudentVerificationResponse(savedVerification);
    }

    @Override
    public StudentVerificationResponse rejectVerification(Long verificationId, Long adminId, String reason) {
        logger.info("Rejecting student verification: {} by admin: {}", verificationId, adminId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findById(verificationId);
        if (verificationOpt.isEmpty()) {
            throw new VerificationFailedException("Verification not found: " + verificationId);
        }
        
        StudentVerification verification = verificationOpt.get();
        if (verification.getStatus() != StudentVerification.VerificationStatus.PENDING) {
            throw new VerificationFailedException("Verification is not pending: " + verificationId);
        }
        
        // Update verification status
        verification.setStatus(StudentVerification.VerificationStatus.REJECTED);
        verification.setReviewedBy(adminId);
        verification.setReviewedAt(LocalDateTime.now());
        verification.setRejectionReason(reason);
        verification.setUpdatedAt(LocalDateTime.now());
        
        StudentVerification savedVerification = verificationRepository.save(verification);
        
        // Publish event
        eventPublisher.publishStudentVerificationRejectedEvent(verification.getUserId(), verificationId, reason);
        
        logger.info("Student verification rejected: {}", verificationId);
        return verificationMapper.toStudentVerificationResponse(savedVerification);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentVerificationResponse> getVerificationById(Long verificationId) {
        logger.debug("Getting verification by ID: {}", verificationId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findById(verificationId);
        return verificationOpt.map(verificationMapper::toStudentVerificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentVerificationResponse> getVerificationByUserId(Long userId) {
        logger.debug("Getting verification by user ID: {}", userId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findByUserId(userId);
        return verificationOpt.map(verificationMapper::toStudentVerificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentVerificationResponse> getPendingVerifications(Pageable pageable) {
        logger.debug("Getting pending verifications");
        
        Page<StudentVerification> verifications = verificationRepository
                .findByStatusOrderBySubmittedAtAsc(StudentVerification.VerificationStatus.PENDING, pageable);
        
        return verifications.map(verificationMapper::toStudentVerificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentVerificationResponse> getVerificationsByStatus(
            StudentVerification.VerificationStatus status, Pageable pageable) {
        logger.debug("Getting verifications by status: {}", status);
        
        Page<StudentVerification> verifications = verificationRepository
                .findByStatusOrderBySubmittedAtDesc(status, pageable);
        
        return verifications.map(verificationMapper::toStudentVerificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserVerified(Long userId) {
        logger.debug("Checking if user is verified: {}", userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getIsVerified).orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public long getVerificationCount(StudentVerification.VerificationStatus status) {
        logger.debug("Getting verification count for status: {}", status);
        
        return verificationRepository.countByStatus(status);
    }

    @Override
    public void resendVerificationEmail(Long verificationId) {
        logger.info("Resending verification email for: {}", verificationId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findById(verificationId);
        if (verificationOpt.isEmpty()) {
            throw new VerificationFailedException("Verification not found: " + verificationId);
        }
        
        StudentVerification verification = verificationOpt.get();
        if (verification.getStatus() != StudentVerification.VerificationStatus.PENDING) {
            throw new VerificationFailedException("Cannot resend email for non-pending verification");
        }
        
        // Generate new token
        verification.setVerificationToken(generateVerificationToken());
        verification.setUpdatedAt(LocalDateTime.now());
        verificationRepository.save(verification);
        
        // Publish event to send email
        eventPublisher.publishVerificationEmailResendEvent(verification.getUserId(), verification.getVerificationToken());
        
        logger.info("Verification email resent for: {}", verificationId);
    }

    @Override
    public StudentVerificationResponse updateVerificationDocuments(Long verificationId, 
                                                                 StudentVerificationRequest request) {
        logger.info("Updating verification documents for: {}", verificationId);
        
        Optional<StudentVerification> verificationOpt = verificationRepository.findById(verificationId);
        if (verificationOpt.isEmpty()) {
            throw new VerificationFailedException("Verification not found: " + verificationId);
        }
        
        StudentVerification verification = verificationOpt.get();
        if (verification.getStatus() != StudentVerification.VerificationStatus.PENDING) {
            throw new VerificationFailedException("Cannot update non-pending verification");
        }
        
        // Update verification details
        verificationMapper.updateVerificationFromRequest(verification, request);
        verification.setUpdatedAt(LocalDateTime.now());
        
        StudentVerification savedVerification = verificationRepository.save(verification);
        
        logger.info("Verification documents updated: {}", verificationId);
        return verificationMapper.toStudentVerificationResponse(savedVerification);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
