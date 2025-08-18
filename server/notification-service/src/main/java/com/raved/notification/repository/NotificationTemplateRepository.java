package com.raved.notification.repository;

import com.raved.notification.model.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * NotificationTemplateRepository for TheRavedApp MongoDB Converted from JPA to
 * MongoDB repository
 */
@Repository
public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplate, String> {

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
     * Find templates by name pattern (MongoDB regex)
     */
    @Query("{'templateName': {'$regex': ?0, '$options': 'i'}, 'isActive': true}")
    List<NotificationTemplate> findByTemplateNameContainingAndIsActiveTrue(String namePattern);

    /**
     * Find paginated templates
     */
    Page<NotificationTemplate> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Count templates by type
     */
    long countByTemplateTypeAndIsActiveTrue(NotificationTemplate.TemplateType templateType);

    /**
     * Get template statistics by type (MongoDB aggregation - simplified)
     */
    @Query(value = "{'isActive': true}", fields = "{'templateType': 1}")
    List<NotificationTemplate> getActiveTemplatesForStats();

    /**
     * Additional MongoDB-specific methods
     */
    /**
     * Find templates by multiple types
     */
    @Query("{'templateType': {'$in': ?0}, 'isActive': true}")
    List<NotificationTemplate> findByTemplateTypesAndIsActiveTrue(List<NotificationTemplate.TemplateType> templateTypes);

    /**
     * Find templates with variables containing specific keys
     */
    @Query("{'variables.?0': {'$exists': true}, 'isActive': true}")
    List<NotificationTemplate> findByVariableKeyAndIsActiveTrue(String variableKey);
}
