package com.raved.social.repository;

import com.raved.social.model.LeaderboardSeason;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository for LeaderboardSeason operations
 *
 * Provides data access methods for leaderboard season management. Converted
 * from JPA repository to MongoDB repository.
 */
public interface LeaderboardSeasonRepository extends MongoRepository<LeaderboardSeason, String> {

    // Basic CRUD operations
    Optional<LeaderboardSeason> findByName(String name);

    List<LeaderboardSeason> findByStatus(String status);

    List<LeaderboardSeason> findBySeasonType(String seasonType);

    List<LeaderboardSeason> findByCategory(String category);

    // Status-based queries
    List<LeaderboardSeason> findByStatusAndIsPublic(String status, Boolean isPublic);

    List<LeaderboardSeason> findByStatusAndCategory(String status, String category);

    // Date-based queries
    List<LeaderboardSeason> findByStartDateAfter(LocalDateTime date);

    List<LeaderboardSeason> findByEndDateBefore(LocalDateTime date);

    List<LeaderboardSeason> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<LeaderboardSeason> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Active seasons queries
    @Query(value = "{'status': 'ACTIVE', 'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}}")
    List<LeaderboardSeason> findCurrentlyActiveSeasons(LocalDateTime now);

    @Query(value = "{'status': 'UPCOMING', 'startDate': {$gte: ?0}}")
    List<LeaderboardSeason> findUpcomingSeasons(LocalDateTime now);

    @Query(value = "{'status': 'COMPLETED', 'endDate': {$lte: ?0}}")
    List<LeaderboardSeason> findCompletedSeasons(LocalDateTime now);

    // Registration queries
    @Query(value = "{'status': 'ACTIVE', 'registrationStartDate': {$lte: ?0}, 'registrationEndDate': {$gte: ?0}}")
    List<LeaderboardSeason> findSeasonsWithOpenRegistration(LocalDateTime now);

    List<LeaderboardSeason> findByIsRegistrationRequired(Boolean isRegistrationRequired);

    List<LeaderboardSeason> findByIsAutoEnrollment(Boolean isAutoEnrollment);

    // Creator and moderator queries
    List<LeaderboardSeason> findByCreatedBy(String createdBy);

    List<LeaderboardSeason> findByModeratedBy(String moderatedBy);

    // Public visibility queries
    List<LeaderboardSeason> findByIsPublic(Boolean isPublic);

    List<LeaderboardSeason> findByIsPublicAndStatus(Boolean isPublic, String status);

    // Timezone queries
    List<LeaderboardSeason> findByTimezone(String timezone);

    // Participant limit queries
    List<LeaderboardSeason> findByMaxParticipantsGreaterThan(Integer minParticipants);

    List<LeaderboardSeason> findByMaxParticipantsLessThan(Integer maxParticipants);

    // Search by name pattern
    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    List<LeaderboardSeason> findByNameContainingIgnoreCase(String namePattern);

    // Find seasons by eligibility criteria
    @Query(value = "{'eligibilityCriteria': {$regex: ?0, $options: 'i'}}")
    List<LeaderboardSeason> findByEligibilityCriteriaContaining(String criteria);

    // Find seasons by theme
    List<LeaderboardSeason> findBySeasonTheme(String theme);

    // Find seasons by logo
    List<LeaderboardSeason> findBySeasonLogo(String logo);

    // Find seasons by color
    List<LeaderboardSeason> findBySeasonColor(String color);

    // Complex queries
    @Query(value = "{'status': ?0, 'category': ?1, 'isPublic': ?2, 'startDate': {$gte: ?3}}")
    List<LeaderboardSeason> findSeasonsByStatusCategoryAndDate(String status, String category, Boolean isPublic, LocalDateTime startDate);

    // Count queries
    long countByStatus(String status);

    long countByStatusAndCategory(String status, String category);

    long countBySeasonType(String seasonType);

    long countByIsPublic(Boolean isPublic);

    // Exists queries
    boolean existsByName(String name);

    boolean existsByNameAndStatus(String name, String status);

    // Delete operations
    void deleteByStatus(String status);

    void deleteByCategory(String category);

    void deleteByCreatedBy(String createdBy);

    // Custom queries for analytics
    @Query(value = "{}", fields = "{'name': 1, 'status': 1, 'startDate': 1, 'endDate': 1, 'category': 1}")
    List<LeaderboardSeason> findSeasonSummary();

    // Find seasons requiring moderation
    @Query(value = "{'status': 'ACTIVE', 'moderatedBy': {$exists: false}}")
    List<LeaderboardSeason> findSeasonsRequiringModeration();

    // Find seasons with specific rules
    @Query(value = "{'rules': {$exists: true, $ne: {}}}")
    List<LeaderboardSeason> findSeasonsWithCustomRules();

    // Find seasons with rewards
    @Query(value = "{'rewards': {$exists: true, $ne: {}}}")
    List<LeaderboardSeason> findSeasonsWithRewards();
}
