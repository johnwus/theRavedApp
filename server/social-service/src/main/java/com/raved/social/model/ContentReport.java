package com.raved.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a content report (abuse/report flow)
 */
@Entity
@Table(name = "content_reports", 
       indexes = {
           @Index(name = "idx_reports_target", columnList = "target_id, target_type"),
           @Index(name = "idx_reports_reporter", columnList = "reporter_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;
    
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum TargetType {
        POST, COMMENT, PRODUCT
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
