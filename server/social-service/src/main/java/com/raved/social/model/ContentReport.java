package com.raved.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * ContentReport Document for TheRavedApp MongoDB
 *
 * Represents a content report (abuse/report flow). Converted from JPA entity to
 * MongoDB document.
 */
@Document(collection = "content_reports")
@CompoundIndexes({
    @CompoundIndex(name = "target_idx", def = "{'targetId': 1, 'targetType': 1}"),
    @CompoundIndex(name = "reporter_idx", def = "{'reporterId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "status_created_idx", def = "{'status': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "priority_created_idx", def = "{'priority': 1, 'createdAt': -1}")
})
public class ContentReport {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "Reporter ID is required")
    private String reporterId;
    
    @Indexed
    @NotBlank(message = "Target ID is required")
    private String targetId;
    
    @Indexed
    @NotBlank(message = "Target type is required")
    private String targetType;
    
    @Indexed
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    // Additional MongoDB-specific fields for enhanced content moderation
    private LocalDateTime updatedAt;

    @Indexed
    private String status = "PENDING"; // PENDING, UNDER_REVIEW, RESOLVED, DISMISSED

    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    private String category; // SPAM, HARASSMENT, INAPPROPRIATE, COPYRIGHT, etc.

    private String subcategory; // More specific classification

    private String assignedTo; // Moderator assigned to review

    private LocalDateTime assignedAt; // When assigned to moderator

    private LocalDateTime reviewedAt; // When reviewed

    private String reviewNotes; // Moderator's review notes

    private String actionTaken; // Action taken (REMOVE, WARN, SUSPEND, etc.)

    private String resolutionNotes; // Final resolution notes

    private String reporterEmail; // Reporter's contact email

    private String reporterPhone; // Reporter's contact phone

    private Boolean isAnonymous = false; // Anonymous report

    private String reportSource; // APP, WEB, API, etc.

    private String userAgent; // Browser/client information

    private String ipAddress; // IP address for analytics

    private String location; // Geographic location if applicable

    private Map<String, Object> evidence; // Supporting evidence

    private Map<String, Object> metadata; // Additional custom fields

    private String reportHash; // Unique hash for duplicate detection

    private Integer duplicateCount = 0; // Number of similar reports

    private String relatedReportIds; // IDs of related reports

    private Boolean isEscalated = false; // Escalated to higher level

    private String escalationReason; // Reason for escalation

    private LocalDateTime escalatedAt; // When escalated

    // Default constructor
    public ContentReport() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public ContentReport(String reporterId, String targetId, String targetType, String reason) {
        this();
        this.reporterId = reporterId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.reason = reason;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getReportSource() {
        return reportSource;
    }

    public void setReportSource(String reportSource) {
        this.reportSource = reportSource;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> getEvidence() {
        return evidence;
    }

    public void setEvidence(Map<String, Object> evidence) {
        this.evidence = evidence;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getReportHash() {
        return reportHash;
    }

    public void setReportHash(String reportHash) {
        this.reportHash = reportHash;
    }

    public Integer getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(Integer duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    public String getRelatedReportIds() {
        return relatedReportIds;
    }

    public void setRelatedReportIds(String relatedReportIds) {
        this.relatedReportIds = relatedReportIds;
    }

    public Boolean getIsEscalated() {
        return isEscalated;
    }

    public void setIsEscalated(Boolean isEscalated) {
        this.isEscalated = isEscalated;
    }

    public String getEscalationReason() {
        return escalationReason;
    }

    public void setEscalationReason(String escalationReason) {
        this.escalationReason = escalationReason;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(LocalDateTime escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    // Business logic methods
    public void assignToModerator(String moderatorId) {
        this.assignedTo = moderatorId;
        this.assignedAt = LocalDateTime.now();
        this.status = "UNDER_REVIEW";
        this.updatedAt = LocalDateTime.now();
    }

    public void review(String notes, String action) {
        this.reviewNotes = notes;
        this.actionTaken = action;
        this.reviewedAt = LocalDateTime.now();
        this.status = "RESOLVED";
        this.updatedAt = LocalDateTime.now();
    }

    public void dismiss(String reason) {
        this.resolutionNotes = reason;
        this.status = "DISMISSED";
        this.updatedAt = LocalDateTime.now();
    }

    public void escalate(String reason) {
        this.isEscalated = true;
        this.escalationReason = reason;
        this.escalatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementDuplicateCount() {
        this.duplicateCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isHighPriority() {
        return "HIGH".equals(this.priority) || "URGENT".equals(this.priority);
    }

    public boolean isUrgent() {
        return "URGENT".equals(this.priority);
    }

    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public boolean isUnderReview() {
        return "UNDER_REVIEW".equals(this.status);
    }
    
    public boolean isResolved() {
        return "RESOLVED".equals(this.status);
    }
    
    public boolean isDismissed() {
        return "DISMISSED".equals(this.status);
    }
    
    @Override
    public String toString() {
        return "ContentReport{"
                + "id='" + id + '\''
                + ", targetId='" + targetId + '\''
                + ", targetType='" + targetType + '\''
                + ", status='" + status + '\''
                + ", priority='" + priority + '\''
                + ", category='" + category + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}
