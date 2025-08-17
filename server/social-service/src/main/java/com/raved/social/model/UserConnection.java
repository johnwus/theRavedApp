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
 * UserConnection Document for TheRavedApp MongoDB
 *
 * Represents a user connection (friend-like relationship). Converted from JPA
 * entity to MongoDB document.
 */
@Document(collection = "user_connections")
@CompoundIndexes({
    @CompoundIndex(name = "user_connection_idx", def = "{'userId': 1, 'connectedUserId': 1}", unique = true),
    @CompoundIndex(name = "connection_type_idx", def = "{'connectionType': 1, 'status': 1}"),
    @CompoundIndex(name = "status_created_idx", def = "{'status': 1, 'createdAt': -1}")
})
public class UserConnection {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @Indexed
    @NotBlank(message = "Connected user ID is required")
    private String connectedUserId;
    
    @Indexed
    @NotBlank(message = "Connection type is required")
    private String connectionType = "FRIEND";
    
    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;
    
    // Additional MongoDB-specific fields for enhanced social features
    private LocalDateTime updatedAt;
    
    @Indexed
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED, BLOCKED

    private String requestMessage; // Message sent with connection request

    private String responseMessage; // Response message if rejected

    private LocalDateTime acceptedAt; // When connection was accepted

    private LocalDateTime rejectedAt; // When connection was rejected

    private String rejectedBy; // Who rejected the connection

    private String rejectionReason; // Reason for rejection

    private String connectionSource; // APP, WEB, SUGGESTION, etc.

    private Map<String, Object> connectionMetadata; // Additional connection data

    private String mutualConnections; // Number of mutual connections

    private String connectionStrength = "WEAK"; // WEAK, MEDIUM, STRONG

    private LocalDateTime lastInteractionAt; // Last interaction between users

    private String interactionFrequency = "LOW"; // LOW, MEDIUM, HIGH

    private Boolean isFavorite = false; // Marked as favorite connection

    private String notes; // Personal notes about the connection

    private Map<String, Object> metadata; // Additional custom fields

    // Default constructor
    public UserConnection() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public UserConnection(String userId, String connectedUserId, String connectionType) {
        this();
        this.userId = userId;
        this.connectedUserId = connectedUserId;
        this.connectionType = connectionType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConnectedUserId() {
        return connectedUserId;
    }

    public void setConnectedUserId(String connectedUserId) {
        this.connectedUserId = connectedUserId;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
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

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getConnectionSource() {
        return connectionSource;
    }

    public void setConnectionSource(String connectionSource) {
        this.connectionSource = connectionSource;
    }

    public Map<String, Object> getConnectionMetadata() {
        return connectionMetadata;
    }

    public void setConnectionMetadata(Map<String, Object> connectionMetadata) {
        this.connectionMetadata = connectionMetadata;
    }

    public String getMutualConnections() {
        return mutualConnections;
    }

    public void setMutualConnections(String mutualConnections) {
        this.mutualConnections = mutualConnections;
    }

    public String getConnectionStrength() {
        return connectionStrength;
    }

    public void setConnectionStrength(String connectionStrength) {
        this.connectionStrength = connectionStrength;
    }

    public LocalDateTime getLastInteractionAt() {
        return lastInteractionAt;
    }

    public void setLastInteractionAt(LocalDateTime lastInteractionAt) {
        this.lastInteractionAt = lastInteractionAt;
    }

    public String getInteractionFrequency() {
        return interactionFrequency;
    }

    public void setInteractionFrequency(String interactionFrequency) {
        this.interactionFrequency = interactionFrequency;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    // Business logic methods
    public void accept() {
        this.status = "ACCEPTED";
        this.acceptedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(String reason, String rejectedBy) {
        this.status = "REJECTED";
        this.rejectionReason = reason;
        this.rejectedBy = rejectedBy;
        this.rejectedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void block() {
        this.status = "BLOCKED";
        this.updatedAt = LocalDateTime.now();
    }

    public void unblock() {
        this.status = "ACCEPTED";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateLastInteraction() {
        this.lastInteractionAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsFavorite() {
        this.isFavorite = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeFavorite() {
        this.isFavorite = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public boolean isAccepted() {
        return "ACCEPTED".equals(this.status);
    }

    public boolean isRejected() {
        return "REJECTED".equals(this.status);
    }

    public boolean isBlocked() {
        return "BLOCKED".equals(this.status);
    }

    @Override
    public String toString() {
        return "UserConnection{"
                + "id='" + id + '\''
                + ", userId='" + userId + '\''
                + ", connectedUserId='" + connectedUserId + '\''
                + ", connectionType='" + connectionType + '\''
                + ", status='" + status + '\''
                + ", isFavorite=" + isFavorite
                + ", createdAt=" + createdAt
                + '}';
    }
}
