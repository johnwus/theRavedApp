package com.raved.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a user connection (friend-like relationship)
 */
@Entity
@Table(name = "user_connections", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "addressee_id"}),
       indexes = {
           @Index(name = "idx_connections_requester", columnList = "requester_id"),
           @Index(name = "idx_connections_addressee", columnList = "addressee_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;
    
    @Column(name = "addressee_id", nullable = false)
    private Long addresseeId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false)
    private ConnectionStatus connectionStatus = ConnectionStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type", nullable = false)
    private ConnectionType connectionType = ConnectionType.FRIEND;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum ConnectionStatus {
        PENDING, ACCEPTED, BLOCKED
    }
    
    public enum ConnectionType {
        FRIEND, STUDY_BUDDY, ROOMMATE
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
