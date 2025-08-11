package com.raved.user.mapper;

import com.raved.user.dto.request.StudentVerificationRequest;
import com.raved.user.dto.response.StudentVerificationResponse;
import com.raved.user.model.StudentVerification;
import org.springframework.stereotype.Component;

/**
 * Mapper for StudentVerification entities and DTOs
 */
@Component
public class StudentVerificationMapper {

    /**
     * Convert StudentVerificationRequest to StudentVerification entity
     */
    public StudentVerification toStudentVerification(StudentVerificationRequest request) {
        if (request == null) {
            return null;
        }

        StudentVerification verification = new StudentVerification();
        // Note: User and University entities need to be set separately
        verification.setStudentIdDocumentUrl(request.getStudentIdDocumentUrl());
        verification.setAdditionalDocumentUrl(request.getAdditionalDocumentUrl());
        
        return verification;
    }

    /**
     * Convert StudentVerification entity to StudentVerificationResponse
     */
    public StudentVerificationResponse toStudentVerificationResponse(StudentVerification verification) {
        if (verification == null) {
            return null;
        }

        StudentVerificationResponse response = new StudentVerificationResponse();
        response.setId(verification.getId());
        response.setUserId(verification.getUser() != null ? verification.getUser().getId() : null);
        response.setUniversityId(verification.getUniversity() != null ? verification.getUniversity().getId() : null);
        response.setStudentIdDocumentUrl(verification.getStudentIdDocumentUrl());
        response.setAdditionalDocumentUrl(verification.getAdditionalDocumentUrl());
        response.setStatus(verification.getVerificationStatus());
        response.setSubmittedAt(verification.getSubmittedAt());
        response.setReviewedAt(verification.getVerifiedAt());
        response.setReviewedBy(verification.getVerifiedByUserId());
        response.setRejectionReason(verification.getRejectionReason());
        response.setCreatedAt(verification.getSubmittedAt());
        response.setUpdatedAt(verification.getUpdatedAt());
        
        return response;
    }

    /**
     * Update StudentVerification entity from StudentVerificationRequest
     */
    public void updateVerificationFromRequest(StudentVerification verification, StudentVerificationRequest request) {
        if (verification == null || request == null) {
            return;
        }

        verification.setStudentIdDocumentUrl(request.getStudentIdDocumentUrl());
        verification.setAdditionalDocumentUrl(request.getAdditionalDocumentUrl());
    }
}
