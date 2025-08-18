package com.raved.user.dto.response;

import com.raved.user.model.StudentVerification;

import java.time.LocalDateTime;

/**
 * Response DTO for student verification
 */
public class StudentVerificationResponse {

    private Long id;
    private Long userId;
    private Long universityId;
    private String studentIdDocumentUrl;
    private String additionalDocumentUrl;
    private StudentVerification.VerificationStatus status;
    private String verificationToken;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public StudentVerificationResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getStudentIdDocumentUrl() {
        return studentIdDocumentUrl;
    }

    public void setStudentIdDocumentUrl(String studentIdDocumentUrl) {
        this.studentIdDocumentUrl = studentIdDocumentUrl;
    }

    public String getAdditionalDocumentUrl() {
        return additionalDocumentUrl;
    }

    public void setAdditionalDocumentUrl(String additionalDocumentUrl) {
        this.additionalDocumentUrl = additionalDocumentUrl;
    }

    public StudentVerification.VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(StudentVerification.VerificationStatus status) {
        this.status = status;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
