package com.raved.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for student verification
 */
public class StudentVerificationRequest {

    @NotNull(message = "University ID is required")
    private Long universityId;

    @NotBlank(message = "Student ID document URL is required")
    private String studentIdDocumentUrl;

    private String additionalDocumentUrl;

    // Constructors
    public StudentVerificationRequest() {}

    public StudentVerificationRequest(Long universityId, String studentIdDocumentUrl) {
        this.universityId = universityId;
        this.studentIdDocumentUrl = studentIdDocumentUrl;
    }

    // Getters and Setters
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
}
