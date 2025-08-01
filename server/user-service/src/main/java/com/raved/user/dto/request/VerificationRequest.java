package com.raved.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for student verification request
 */
public class VerificationRequest {

    @NotNull(message = "University ID is required")
    private Long universityId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    private String verificationDocumentUrl;

    // Constructors
    public VerificationRequest() {}

    public VerificationRequest(Long universityId, String studentId) {
        this.universityId = universityId;
        this.studentId = studentId;
    }

    // Getters and Setters
    public Long getUniversityId() { return universityId; }
    public void setUniversityId(Long universityId) { this.universityId = universityId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getVerificationDocumentUrl() { return verificationDocumentUrl; }
    public void setVerificationDocumentUrl(String verificationDocumentUrl) { this.verificationDocumentUrl = verificationDocumentUrl; }

    @Override
    public String toString() {
        return "VerificationRequest{" +
                "universityId=" + universityId +
                ", studentId='" + studentId + '\'' +
                ", verificationDocumentUrl='" + verificationDocumentUrl + '\'' +
                '}';
    }
}
