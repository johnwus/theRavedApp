package com.raved.user.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for StudentVerification entity
 */
public class StudentVerificationResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private Long universityId;
    private String universityName;
    private String studentIdDocumentUrl;
    private String additionalDocumentUrl;
    private String verificationStatus;
    private Long verifiedByUserId;
    private String verifiedByUserName;
    private String verificationNotes;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;
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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
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

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Long getVerifiedByUserId() {
        return verifiedByUserId;
    }

    public void setVerifiedByUserId(Long verifiedByUserId) {
        this.verifiedByUserId = verifiedByUserId;
    }

    public String getVerifiedByUserName() {
        return verifiedByUserName;
    }

    public void setVerifiedByUserName(String verifiedByUserName) {
        this.verifiedByUserName = verifiedByUserName;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
