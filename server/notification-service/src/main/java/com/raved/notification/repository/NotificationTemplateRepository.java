package com.raved.notification.repository;

import com.raved.notification.model.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    Optional<NotificationTemplate> findByTemplateName(String templateName);

    /**
     * Find templates by type
     */
    List<NotificationTemplate> findByTemplateType(NotificationTemplate.TemplateType templateType);

    /**
     * Find active templates
     */
    List<NotificationTemplate> findByIsActiveTrue();

    /**
     * Find templates by type and active status
     */
    List<NotificationTemplate> findByTemplateTypeAndIsActiveTrueOrderByCreatedAtDesc(NotificationTemplate.TemplateType templateType);

    /**
     * Check if template name exists
     */
    boolean existsByTemplateName(String templateName);

    /**
     * Find templates by name pattern
     */
    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.templateName LIKE %:namePattern% AND nt.isActive = true")
    List<NotificationTemplate> findByTemplateNameContainingAndIsActiveTrue(@Param("namePattern") String namePattern);

    /**
     * Find paginated templates
     */
    Page<NotificationTemplate> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Count templates by type
     */
    long countByTemplateTypeAndIsActiveTrue(NotificationTemplate.TemplateType templateType);

    /**
     * Get template statistics by type
     */
    @Query("SELECT nt.templateType as templateType, COUNT(nt) as count " +
           "FROM NotificationTemplate nt WHERE nt.isActive = true " +
           "GROUP BY nt.templateType")
    List<Object[]> getTemplateStatsByType();
}
