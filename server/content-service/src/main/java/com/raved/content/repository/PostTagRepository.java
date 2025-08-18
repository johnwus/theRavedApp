package com.raved.content.repository;

import com.raved.content.model.PostTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostTagRepository for TheRavedApp MongoDB
 */
@Repository
public interface PostTagRepository extends MongoRepository<PostTag, String> {

    /**
     * Find tag by name
     */
    Optional<PostTag> findByTagName(String tagName);

    /**
     * Find tags by post ID
     */
    List<PostTag> findByPostId(String postId);

    /**
     * Find tags by category
     */
    List<PostTag> findByCategory(String category);

    /**
     * Find tags by status
     */
    List<PostTag> findByStatus(String status);

    /**
     * Find tags with usage count above threshold
     */
    List<PostTag> findByUsageCountGreaterThan(Integer threshold);

    /**
     * Find tags by partial name match
     */
    @Query("{'tagName': {$regex: ?0, $options: 'i'}}")
    List<PostTag> findByTagNameContainingIgnoreCase(String tagName);

    /**
     * Find most popular tags
     */
    @Query(value = "{}", sort = "{'usageCount': -1}")
    List<PostTag> findTopTagsByUsage(int limit);

    /**
     * Find trending tags
     */
    @Query(value = "{}", sort = "{'viewCount': -1, 'clickCount': -1}")
    List<PostTag> findTrendingTags(int limit);

    /**
     * Find tags by language
     */
    List<PostTag> findByLanguage(String language);

    /**
     * Find tags by region
     */
    List<PostTag> findByRegion(String region);

    /**
     * Find tags by creator
     */
    List<PostTag> findByCreatedBy(String createdBy);

    /**
     * Find tags that need moderation
     */
    @Query("{'status': 'PENDING'}")
    List<PostTag> findTagsNeedingModeration();

    /**
     * Find related tags
     */
    @Query("{'relatedTags': {$in: ?0}}")
    List<PostTag> findByRelatedTags(List<String> tagNames);

    /**
     * Find parent tags
     */
    @Query("{'childTags': ?0}")
    List<PostTag> findParentTags(String childTagName);

    /**
     * Find child tags
     */
    @Query("{'parentTags': ?0}")
    List<PostTag> findChildTags(String parentTagName);

    /**
     * Count tags by category
     */
    long countByCategory(String category);

    /**
     * Count tags by status
     */
    long countByStatus(String status);

    /**
     * Count tags by creator
     */
    long countByCreatedBy(String createdBy);

    /**
     * Find tags by metadata
     */
    @Query("{'metadata.?0': ?1}")
    List<PostTag> findByMetadata(String key, Object value);

    /**
     * Find tags by status ordered by usage count descending
     */
    List<PostTag> findByStatusOrderByUsageCountDesc(String status);

    /**
     * Find tags by status ordered by view count descending
     */
    List<PostTag> findByStatusOrderByViewCountDesc(String status);

    /**
     * Find tags by category and tag name not equal
     */
    List<PostTag> findByCategoryAndTagNameNot(String category, String tagName);

    /**
     * Find tags containing child tag
     */
    List<PostTag> findByChildTagsContaining(String childTagName);

    /**
     * Find tags by parent tag
     */
    List<PostTag> findByParentTag(String parentTagName);

    /**
     * Find tags by metadata key and value
     */
    List<PostTag> findByMetadataKeyAndMetadataValue(String key, Object value);
}
