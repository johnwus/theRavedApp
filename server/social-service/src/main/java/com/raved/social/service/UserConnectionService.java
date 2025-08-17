package com.raved.social.service;

import com.raved.social.model.UserConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for UserConnection operations
 *
 * Provides business logic for user connection management. Converted from JPA
 * service to MongoDB service.
 */
public interface UserConnectionService {

    // Basic CRUD operations
    UserConnection createConnection(UserConnection connection);

    UserConnection updateConnection(String id, UserConnection connection);

    Optional<UserConnection> getConnectionById(String id);

    Optional<UserConnection> getConnectionByUsers(String userId, String connectedUserId);

    List<UserConnection> getAllConnections();

    void deleteConnection(String id);

    // User-based operations
    List<UserConnection> getConnectionsByUser(String userId);

    List<UserConnection> getConnectionsByConnectedUser(String connectedUserId);

    List<UserConnection> getAllConnectionsForUser(String userId);

    // Status-based operations
    List<UserConnection> getConnectionsByStatus(String status);

    List<UserConnection> getConnectionsByUserAndStatus(String userId, String status);

    List<UserConnection> getConnectionsByConnectedUserAndStatus(String connectedUserId, String status);

    // Connection type operations
    List<UserConnection> getConnectionsByType(String connectionType);

    List<UserConnection> getConnectionsByUserAndType(String userId, String connectionType);

    List<UserConnection> getConnectionsByConnectedUserAndType(String connectedUserId, String connectionType);

    // Combined criteria operations
    List<UserConnection> getConnectionsByUserStatusAndType(String userId, String status, String connectionType);

    List<UserConnection> getConnectionsByConnectedUserStatusAndType(String connectedUserId, String status, String connectionType);

    // Date-based operations
    List<UserConnection> getConnectionsCreatedAfter(LocalDateTime date);

    List<UserConnection> getConnectionsCreatedBefore(LocalDateTime date);

    List<UserConnection> getConnectionsCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<UserConnection> getConnectionsUpdatedAfter(LocalDateTime date);

    List<UserConnection> getConnectionsUpdatedBefore(LocalDateTime date);

    List<UserConnection> getConnectionsUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Acceptance and rejection operations
    List<UserConnection> getConnectionsAcceptedAfter(LocalDateTime date);

    List<UserConnection> getConnectionsRejectedAfter(LocalDateTime date);

    List<UserConnection> getConnectionsRejectedBy(String rejectedBy);

    // Source operations
    List<UserConnection> getConnectionsBySource(String source);

    List<UserConnection> getConnectionsByUserAndSource(String userId, String source);

    // Favorite operations
    List<UserConnection> getFavoriteConnectionsByUser(String userId);

    List<UserConnection> getFavoriteConnectionsByConnectedUser(String connectedUserId);

    // Interaction operations
    List<UserConnection> getConnectionsWithRecentInteraction(LocalDateTime since);

    List<UserConnection> getConnectionsByInteractionFrequency(String frequency);

    // Strength and metadata operations
    List<UserConnection> getConnectionsByStrength(String strength);

    List<UserConnection> getConnectionsByUserAndStrength(String userId, String strength);

    // Notes operations
    List<UserConnection> searchConnectionsByNotes(String notePattern);

    // Complex operations
    List<UserConnection> getConnectionsByUserAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    List<UserConnection> getConnectionsByUserAndStatusAndDateRange(String userId, String status, LocalDateTime startDate, LocalDateTime endDate);

    // Search operations
    List<UserConnection> searchConnectionsByCriteria(String userId, String status, String connectionType, String source);

    Page<UserConnection> getConnectionsPageable(String userId, Pageable pageable);

    // Count operations
    long getTotalConnections();

    long getConnectionsCountByUser(String userId);

    long getConnectionsCountByStatus(String status);

    long getConnectionsCountByUserAndStatus(String userId, String status);

    long getConnectionsCountByType(String connectionType);

    long getConnectionsCountByUserAndType(String userId, String connectionType);

    // Exists operations
    boolean connectionExistsByUsers(String userId, String connectedUserId);

    boolean connectionExistsByUsersAndType(String userId, String connectedUserId, String connectionType);

    // Delete operations
    void deleteConnectionsByUser(String userId);

    void deleteConnectionsByStatus(String status);

    void deleteConnectionsByType(String connectionType);

    // Bulk operations
    List<UserConnection> bulkUpdateConnections(List<String> connectionIds, UserConnection updates);

    void bulkDeleteConnectionsByStatus(String status);

    void bulkDeleteConnectionsByUser(String userId);

    void bulkDeleteConnectionsByType(String connectionType);

    // Export operations
    List<UserConnection> exportConnectionData();

    List<UserConnection> exportConnectionsByUser(String userId);

    List<UserConnection> exportConnectionsByStatus(String status);

    List<UserConnection> exportConnectionsByType(String connectionType);
}
