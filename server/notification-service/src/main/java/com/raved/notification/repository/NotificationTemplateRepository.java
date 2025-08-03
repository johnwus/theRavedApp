package com.raved.notification.repository;

import com.raved.notification.model.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * NotificationTemplateRepository for TheRavedApp
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    /**
     * Find template by name
     */
    Optional<NotificationTemplate> findByName(String name);

    /**
     * Find template by name and language
     */
    Optional<NotificationTemplate> findByNameAndLanguage(String name, String language);

    /**
     * Find templates by type
     */
    List<NotificationTemplate> findByType(NotificationTemplate.TemplateType type);

    /**
     * Find active templates
     */
    List<NotificationTemplate> findByIsActiveTrue();

    /**
     * Find templates by type and active status
     */
    List<NotificationTemplate> findByTypeAndIsActiveTrueOrderByCreatedAtDesc(NotificationTemplate.TemplateType type);

    /**
     * Find templates by language
     */
    List<NotificationTemplate> findByLanguageAndIsActiveTrue(String language);

    /**
     * Check if template name exists
     */
    boolean existsByName(String name);

    /**
     * Find templates by name pattern
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.name LIKE %:namePattern% AND nt.isActive = true")
    List<NotificationTemplate> findByNameContainingAndIsActiveTrue(@Param("namePattern") String namePattern);

    /**
     * Find latest version of template by name
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.name = :name AND nt.isActive = true " +
           "ORDER BY nt.version DESC")
    List<NotificationTemplate> findLatestVersionByName(@Param("name") String name);

    /**
     * Count templates by type
     */
    long countByTypeAndIsActiveTrue(NotificationTemplate.TemplateType type);

    /**
     * Find paginated templates
     */
    Page<NotificationTemplate> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Get template statistics by type
     */
    @Query("SELECT nt.type as type, COUNT(nt) as count " +
           "FROM NotificationTemplate nt WHERE nt.isActive = true " +
           "GROUP BY nt.type")
    List<Object[]> getTemplateStatsByType();
}
