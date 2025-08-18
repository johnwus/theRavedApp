package com.raved.user.service;

import com.raved.user.dto.request.StudentVerificationRequest;
import com.raved.user.dto.response.StudentVerificationResponse;
import com.raved.user.model.StudentVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * StudentVerificationService for TheRavedApp
 */
public interface StudentVerificationService {

    StudentVerificationResponse submitVerification(StudentVerificationRequest request);
    
    StudentVerificationResponse approveVerification(Long verificationId, Long adminId);
    
    StudentVerificationResponse rejectVerification(Long verificationId, Long adminId, String reason);
    
    Optional<StudentVerificationResponse> getVerificationById(Long verificationId);
    
    Optional<StudentVerificationResponse> getVerificationByUserId(Long userId);
    
    Page<StudentVerificationResponse> getPendingVerifications(Pageable pageable);
    
    Page<StudentVerificationResponse> getVerificationsByStatus(StudentVerification.VerificationStatus status, Pageable pageable);
    
    boolean isUserVerified(Long userId);
    
    long getVerificationCount(StudentVerification.VerificationStatus status);
    
    void resendVerificationEmail(Long verificationId);
    
    StudentVerificationResponse updateVerificationDocuments(Long verificationId, StudentVerificationRequest request);
}
