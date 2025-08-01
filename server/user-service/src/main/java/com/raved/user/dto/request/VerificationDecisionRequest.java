package com.raved.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for verification decision (approve/reject)
 */
public class VerificationDecisionRequest {

    @NotNull(message = "Decision is required")
    private Boolean approved;

    private String notes;

    private String rejectionReason;

    // Constructors
    public VerificationDecisionRequest() {}

    public VerificationDecisionRequest(Boolean approved, String notes) {
        this.approved = approved;
        this.notes = notes;
    }

    // Getters and Setters
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
