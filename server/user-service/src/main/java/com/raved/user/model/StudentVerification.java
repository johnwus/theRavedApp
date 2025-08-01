package com.raved.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * StudentVerification Entity for TheRavedApp
 *
 * Represents student verification records for university authentication.
 * Based on the student_verifications table schema.
 */
@Entity
@Table(name = "student_verifications", indexes = {
        @Index(name = "idx_student_verifications_user", columnList = "user_id"),
        @Index(name = "idx_student_verifications_university", columnList = "university_id"),
        @Index(name = "idx_student_verifications_status", columnList = "verification_status"),
        @Index(name = "idx_student_verifications_submitted", columnList = "submitted_at")
})
public class StudentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @NotBlank(message = "Student ID document URL is required")
    @Column(name = "student_id_document_url", columnDefinition = "TEXT", nullable = false)
    private String studentIdDocumentUrl;

    @Column(name = "additional_document_url", columnDefinition = "TEXT")
    private String additionalDocumentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "verified_by_user_id")
    private Long verifiedByUserId;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public StudentVerification() {
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public StudentVerification(User user, University university, String studentIdDocumentUrl) {
        this();
        this.user = user;
        this.university = university;
        this.studentIdDocumentUrl = studentIdDocumentUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
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

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Long getVerifiedByUserId() {
        return verifiedByUserId;
    }

    public void setVerifiedByUserId(Long verifiedByUserId) {
        this.verifiedByUserId = verifiedByUserId;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
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

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
    public void approve(Long verifiedByUserId, String notes) {
        this.verificationStatus = VerificationStatus.APPROVED;
        this.verifiedByUserId = verifiedByUserId;
        this.verificationNotes = notes;
        this.verifiedAt = LocalDateTime.now();
    }

    public void reject(Long verifiedByUserId, String rejectionReason) {
        this.verificationStatus = VerificationStatus.REJECTED;
        this.verifiedByUserId = verifiedByUserId;
        this.rejectionReason = rejectionReason;
        this.verifiedAt = LocalDateTime.now();
    }

    public boolean isApproved() {
        return verificationStatus == VerificationStatus.APPROVED;
    }

    public boolean isPending() {
        return verificationStatus == VerificationStatus.PENDING;
    }

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED, RESUBMISSION_REQUIRED
    }

    @Override
    public String toString() {
        return "StudentVerification{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", universityId=" + (university != null ? university.getId() : null) +
                ", verificationStatus=" + verificationStatus +
                ", submittedAt=" + submittedAt +
                ", verifiedAt=" + verifiedAt +
                '}';
    }
}
