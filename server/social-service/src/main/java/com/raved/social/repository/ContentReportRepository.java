package com.raved.social.repository;

import com.raved.social.model.ContentReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for ContentReport operations
 * 
 * Provides data access methods for content moderation and reporting.
 * Converted from JPA repository to MongoDB repository.
 */
public interface ContentReportRepository extends MongoRepository<ContentReport, String> {
    
    // Basic CRUD operations
    List<ContentReport> findByReporterId(String reporterId);
    
    List<ContentReport> findByTargetId(String targetId);
    
    List<ContentReport> findByTargetType(String targetType);
    
    // Status-based queries
    List<ContentReport> findByStatus(String status);
    
    List<ContentReport> findByReporterIdAndStatus(String reporterId, String status);
    
    List<ContentReport> findByTargetIdAndStatus(String targetId, String status);
    
    List<ContentReport> findByTargetTypeAndStatus(String targetType, String status);
    
    // Priority queries
    List<ContentReport> findByPriority(String priority);
    
    List<ContentReport> findByPriorityAndStatus(String priority, String status);
    
    List<ContentReport> findByPriorityGreaterThan(String minPriority);
    
    // Category queries
    List<ContentReport> findByCategory(String category);
    
    List<ContentReport> findByCategoryAndStatus(String category, String status);
    
    List<ContentReport> findBySubcategory(String subcategory);
    
    List<ContentReport> findByCategoryAndSubcategory(String category, String subcategory);
    
    // Assignment queries
    List<ContentReport> findByAssignedTo(String assignedTo);
    
    List<ContentReport> findByAssignedToAndStatus(String assignedTo, String status);
    
    List<ContentReport> findByAssignedToIsNull();
    
    // Date-based queries
    List<ContentReport> findByCreatedAtAfter(LocalDateTime date);
    
    List<ContentReport> findByCreatedAtBefore(LocalDateTime date);
    
    List<ContentReport> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<ContentReport> findByUpdatedAtAfter(LocalDateTime date);
    
    List<ContentReport> findByUpdatedAtBefore(LocalDateTime date);
    
    List<ContentReport> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<ContentReport> findByAssignedAtAfter(LocalDateTime date);
    
    List<ContentReport> findByReviewedAtAfter(LocalDateTime date);
    
    List<ContentReport> findByEscalatedAtAfter(LocalDateTime date);
    
    // Source queries
    List<ContentReport> findByReportSource(String source);
    
    List<ContentReport> findByReporterIdAndReportSource(String reporterId, String source);
    
    // Anonymous queries
    List<ContentReport> findByIsAnonymous(Boolean isAnonymous);
    
    List<ContentReport> findByIsAnonymousAndStatus(Boolean isAnonymous, String status);
    
    // Geographic queries
    List<ContentReport> findByLocation(String location);

    // Evidence and metadata queries
    @Query(value = "{'evidence': {$exists: true, $ne: {}}}")
    List<ContentReport> findReportsWithEvidence();
    
    @Query(value = "{'metadata': {$exists: true, $ne: {}}}")
    List<ContentReport> findReportsWithMetadata();
    
    // Hash and duplicate queries
    List<ContentReport> findByReportHash(String reportHash);
    
    List<ContentReport> findByDuplicateCountGreaterThan(Integer threshold);
    
    List<ContentReport> findByRelatedReportIds(String relatedReportId);
    
    // Escalation queries
    List<ContentReport> findByIsEscalated(Boolean isEscalated);
    
    List<ContentReport> findByIsEscalatedAndStatus(Boolean isEscalated, String status);
    
    // Complex queries
    @Query(value = "{'status': 'PENDING', 'priority': {$in: ['HIGH', 'URGENT']}}")
    List<ContentReport> findHighPriorityPendingReports();
    
    @Query(value = "{'status': 'UNDER_REVIEW', 'assignedAt': {$lte: ?0}}")
    List<ContentReport> findOverdueAssignedReports(LocalDateTime threshold);
    
    @Query(value = "{'status': 'PENDING', 'category': ?0, 'priority': ?1}")
    List<ContentReport> findReportsByCategoryAndPriority(String category, String priority);
    
    // Search by reason
    @Query(value = "{'reason': {$regex: ?0, $options: 'i'}}")
    List<ContentReport> findByReasonContaining(String reasonPattern);
    
    // Search by review notes
    @Query(value = "{'reviewNotes': {$regex: ?0, $options: 'i'}}")
    List<ContentReport> findByReviewNotesContaining(String notesPattern);
    
    // Search by resolution notes
    @Query(value = "{'resolutionNotes': {$regex: ?0, $options: 'i'}}")
    List<ContentReport> findByResolutionNotesContaining(String notesPattern);
    
    // Count queries
    long countByStatus(String status);
    
    long countByStatusAndCategory(String status, String category);
    
    long countByPriority(String priority);
    
    long countByReporterId(String reporterId);
    
    long countByTargetId(String targetId);
    
    long countByTargetType(String targetType);
    
    long countByAssignedTo(String assignedTo);
    
    long countByIsAnonymous(Boolean isAnonymous);
    
    long countByIsEscalated(Boolean isEscalated);
    
    // Exists queries
    boolean existsByReporterIdAndTargetIdAndTargetType(String reporterId, String targetId, String targetType);
    
    boolean existsByReportHash(String reportHash);
    
    // Delete operations
    void deleteByStatus(String status);
    
    void deleteByReporterId(String reporterId);
    
    void deleteByTargetId(String targetId);
    
    void deleteByTargetType(String targetType);
    
    void deleteByCategory(String category);
    
    // Custom queries for analytics
    @Query(value = "{}", fields = "{'status': 1, 'priority': 1, 'category': 1, 'createdAt': 1, 'assignedTo': 1}")
    List<ContentReport> findReportSummary();
    
    // Find reports requiring immediate attention
    @Query(value = "{'status': 'PENDING', 'priority': 'URGENT', 'createdAt': {$lte: ?0}}")
    List<ContentReport> findUrgentReportsRequiringAttention(LocalDateTime threshold);
    
    // Find reports by moderator performance
    @Query(value = "{'assignedTo': ?0, 'status': 'RESOLVED', 'reviewedAt': {$gte: ?1, $lte: ?2}}")
    List<ContentReport> findReportsResolvedByModeratorInPeriod(String moderatorId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find duplicate reports
    @Query(value = "{'duplicateCount': {$gt: 0}}")
    List<ContentReport> findDuplicateReports();
    
    // Find reports with specific evidence types
    @Query(value = "{'evidence.evidenceType': ?0}")
    List<ContentReport> findReportsByEvidenceType(String evidenceType);
}