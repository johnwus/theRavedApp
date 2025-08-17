package com.raved.social.repository;

import com.raved.social.model.UserConnection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for UserConnection operations
 * 
 * Provides data access methods for user connection management.
 * Converted from JPA repository to MongoDB repository.
 */
public interface UserConnectionRepository extends MongoRepository<UserConnection, String> {
    
    // Basic CRUD operations
    Optional<UserConnection> findByUserIdAndConnectedUserId(String userId, String connectedUserId);
    
    List<UserConnection> findByUserId(String userId);
    
    List<UserConnection> findByConnectedUserId(String connectedUserId);
    
    // Status-based queries
    List<UserConnection> findByUserIdAndStatus(String userId, String status);
    
    List<UserConnection> findByConnectedUserIdAndStatus(String connectedUserId, String status);
    
    List<UserConnection> findByStatus(String status);
    
    // Connection type queries
    List<UserConnection> findByUserIdAndConnectionType(String userId, String connectionType);
    
    List<UserConnection> findByConnectedUserIdAndConnectionType(String connectedUserId, String connectionType);
    
    List<UserConnection> findByConnectionType(String connectionType);
    
    // Combined status and type queries
    List<UserConnection> findByUserIdAndStatusAndConnectionType(String userId, String status, String connectionType);
    
    List<UserConnection> findByConnectedUserIdAndStatusAndConnectionType(String connectedUserId, String status, String connectionType);
    
    // Date-based queries
    List<UserConnection> findByCreatedAtAfter(LocalDateTime date);
    
    List<UserConnection> findByCreatedAtBefore(LocalDateTime date);
    
    List<UserConnection> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<UserConnection> findByUpdatedAtAfter(LocalDateTime date);
    
    List<UserConnection> findByUpdatedAtBefore(LocalDateTime date);
    
    List<UserConnection> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Acceptance and rejection queries
    List<UserConnection> findByAcceptedAtAfter(LocalDateTime date);
    
    List<UserConnection> findByRejectedAtAfter(LocalDateTime date);
    
    List<UserConnection> findByRejectedBy(String rejectedBy);
    
    // Source queries
    List<UserConnection> findByConnectionSource(String source);
    
    List<UserConnection> findByUserIdAndConnectionSource(String userId, String source);
    
    // Favorite queries
    List<UserConnection> findByUserIdAndIsFavorite(String userId, Boolean isFavorite);
    
    List<UserConnection> findByConnectedUserIdAndIsFavorite(String connectedUserId, Boolean isFavorite);
    
    // Interaction queries
    List<UserConnection> findByLastInteractionAtAfter(LocalDateTime date);
    
    List<UserConnection> findByLastInteractionAtBefore(LocalDateTime date);
    
    List<UserConnection> findByLastInteractionAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<UserConnection> findByInteractionFrequency(String frequency);
    
    // Strength and metadata queries
    List<UserConnection> findByConnectionStrength(String strength);
    
    List<UserConnection> findByUserIdAndConnectionStrength(String userId, String strength);
    
    // Notes queries
    @Query(value = "{'notes': {$regex: ?0, $options: 'i'}}")
    List<UserConnection> findByNotesContaining(String notePattern);
    
    // Complex queries
    @Query(value = "{'userId': ?0, 'status': 'ACCEPTED', 'lastInteractionAt': {$gte: ?1}}")
    List<UserConnection> findActiveConnectionsWithRecentInteraction(String userId, LocalDateTime since);
    
    @Query(value = "{'userId': ?0, 'status': 'ACCEPTED', 'isFavorite': true}")
    List<UserConnection> findFavoriteConnections(String userId);
    
    @Query(value = "{'userId': ?0, 'status': 'PENDING'}")
    List<UserConnection> findPendingConnectionRequests(String userId);
    
    @Query(value = "{'connectedUserId': ?0, 'status': 'PENDING'}")
    List<UserConnection> findPendingConnectionRequestsReceived(String userId);
    
    // Mutual connections (bidirectional)
    @Query(value = "{'$or': [{'userId': ?0}, {'connectedUserId': ?0}], 'status': 'ACCEPTED'}")
    List<UserConnection> findAllConnectionsForUser(String userId);
    
    // Count queries
    long countByUserId(String userId);
    
    long countByUserIdAndStatus(String userId, String status);
    
    long countByUserIdAndConnectionType(String userId, String connectionType);
    
    long countByConnectedUserId(String connectedUserId);
    
    long countByConnectedUserIdAndStatus(String connectedUserId, String status);
    
    long countByStatus(String status);
    
    long countByConnectionType(String connectionType);
    
    long countByUserIdAndIsFavorite(String userId, Boolean isFavorite);
    
    // Exists queries
    boolean existsByUserIdAndConnectedUserId(String userId, String connectedUserId);
    
    boolean existsByUserIdAndConnectedUserIdAndStatus(String userId, String connectedUserId, String status);
    
    // Delete operations
    void deleteByUserId(String userId);
    
    void deleteByUserIdAndConnectedUserId(String userId, String connectedUserId);
    
    void deleteByStatus(String status);
    
    void deleteByConnectionType(String connectionType);
    
    // Custom queries for analytics
    @Query(value = "{}", fields = "{'userId': 1, 'connectedUserId': 1, 'status': 1, 'connectionType': 1, 'createdAt': 1}")
    List<UserConnection> findConnectionSummary();
    
    // Find connections requiring attention
    @Query(value = "{'status': 'PENDING', 'createdAt': {$lte: ?0}}")
    List<UserConnection> findOldPendingConnections(LocalDateTime threshold);
    
    // Find inactive connections
    @Query(value = "{'status': 'ACCEPTED', 'lastInteractionAt': {$lte: ?0}}")
    List<UserConnection> findInactiveConnections(LocalDateTime threshold);
    
    // Find connections by metadata
    @Query(value = "{'metadata': {$exists: true, $ne: {}}}")
    List<UserConnection> findConnectionsWithMetadata();
}