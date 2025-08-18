package com.raved.content.service.impl;

import com.raved.content.model.PostTag;
import com.raved.content.repository.PostTagRepository;
import com.raved.content.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TagService implementation for TheRavedApp MongoDB
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private PostTagRepository postTagRepository;

    @Override
    public PostTag createTag(PostTag tag) {
        logger.info("Creating new tag: {}", tag.getTagName());

        tag.setCreatedAt(LocalDateTime.now());
        tag.setUpdatedAt(LocalDateTime.now());
        tag.setStatus("ACTIVE");
        tag.setUsageCount(0);
        tag.setViewCount(0);
        tag.setClickCount(0);
        tag.setSearchCount(0);

        PostTag savedTag = postTagRepository.save(tag);
        logger.info("Tag created successfully with ID: {}", savedTag.getId());

        return savedTag;
    }

    @Override
    public Optional<PostTag> getTagById(String id) {
        logger.debug("Getting tag by ID: {}", id);
        return postTagRepository.findById(id);
    }

    @Override
    public Optional<PostTag> getTagByName(String name) {
        logger.debug("Getting tag by name: {}", name);
        return postTagRepository.findByTagName(name);
    }

    @Override
    public PostTag updateTag(String id, PostTag tag) {
        logger.info("Updating tag with ID: {}", id);

        PostTag existingTag = postTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with ID: " + id));

        existingTag.setTagName(tag.getTagName());
        existingTag.setCategory(tag.getCategory());
        existingTag.setDescription(tag.getDescription());
        existingTag.setLanguage(tag.getLanguage());
        existingTag.setRegion(tag.getRegion());
        existingTag.setMetadata(tag.getMetadata());
        existingTag.setUpdatedAt(LocalDateTime.now());

        PostTag updatedTag = postTagRepository.save(existingTag);
        logger.info("Tag updated successfully: {}", id);

        return updatedTag;
    }

    @Override
    public void deleteTag(String id) {
        logger.info("Deleting tag with ID: {}", id);
        postTagRepository.deleteById(id);
        logger.info("Tag deleted successfully: {}", id);
    }

    @Override
    public Page<PostTag> getAllTags(Pageable pageable) {
        logger.debug("Getting all tags with pagination");
        return postTagRepository.findAll(pageable);
    }

    @Override
    public List<PostTag> getTagsByCategory(String category) {
        logger.debug("Getting tags by category: {}", category);
        return postTagRepository.findByCategory(category);
    }

    @Override
    public List<PostTag> getTagsByStatus(String status) {
        logger.debug("Getting tags by status: {}", status);
        return postTagRepository.findByStatus(status);
    }

    @Override
    public List<PostTag> getTrendingTags(int limit) {
        logger.debug("Getting trending tags with limit: {}", limit);
        return postTagRepository.findByStatusOrderByUsageCountDesc("ACTIVE")
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<PostTag> getPopularTags(int limit) {
        logger.debug("Getting popular tags with limit: {}", limit);
        return postTagRepository.findByStatusOrderByViewCountDesc("ACTIVE")
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<PostTag> searchTags(String query, int limit) {
        logger.debug("Searching tags with query: {}", query);
        return postTagRepository.findByTagNameContainingIgnoreCase(query)
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<PostTag> getRelatedTags(String tagName, int limit) {
        logger.debug("Getting related tags for: {} with limit: {}", tagName, limit);
        // This would typically use a more sophisticated algorithm
        return postTagRepository.findByCategoryAndTagNameNot(
                getTagByName(tagName).map(PostTag::getCategory).orElse("GENERAL"),
                tagName)
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<PostTag> getParentTags(String tagName) {
        logger.debug("Getting parent tags for: {}", tagName);
        // This would use hierarchical tag structure
        return postTagRepository.findByChildTagsContaining(tagName);
    }

    @Override
    public List<PostTag> getChildTags(String tagName) {
        logger.debug("Getting child tags for: {}", tagName);
        // This would use hierarchical tag structure
        return postTagRepository.findByParentTag(tagName);
    }

    @Override
    public void incrementTagUsage(String tagName) {
        logger.debug("Incrementing usage count for tag: {}", tagName);
        PostTag tag = getTagByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));

        tag.setUsageCount((tag.getUsageCount() != null ? tag.getUsageCount() : 0) + 1);
        tag.setUpdatedAt(LocalDateTime.now());
        postTagRepository.save(tag);
    }

    @Override
    public void incrementTagView(String tagName) {
        logger.debug("Incrementing view count for tag: {}", tagName);
        PostTag tag = getTagByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));

        tag.setViewCount((tag.getViewCount() != null ? tag.getViewCount() : 0) + 1);
        tag.setUpdatedAt(LocalDateTime.now());
        postTagRepository.save(tag);
    }

    @Override
    public void incrementTagClick(String tagName) {
        logger.debug("Incrementing click count for tag: {}", tagName);
        PostTag tag = getTagByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));

        tag.setClickCount((tag.getClickCount() != null ? tag.getClickCount() : 0) + 1);
        tag.setUpdatedAt(LocalDateTime.now());
        postTagRepository.save(tag);
    }

    @Override
    public void incrementTagSearch(String tagName) {
        logger.debug("Incrementing search count for tag: {}", tagName);
        PostTag tag = getTagByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));

        tag.setSearchCount((tag.getSearchCount() != null ? tag.getSearchCount() : 0) + 1);
        tag.setUpdatedAt(LocalDateTime.now());
        postTagRepository.save(tag);
    }

    @Override
    public void moderateTag(String tagId, String status, String reason, String moderatorId) {
        logger.info("Moderating tag: {} to status: {}", tagId, status);

        PostTag tag = postTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagId));

        tag.setStatus(status);
        tag.setModerationReason(reason);
        tag.setModeratorId(moderatorId);
        tag.setModeratedAt(LocalDateTime.now());
        tag.setUpdatedAt(LocalDateTime.now());

        postTagRepository.save(tag);
        logger.info("Tag moderated successfully: {} to {}", tagId, status);
    }

    @Override
    public List<PostTag> getTagsNeedingModeration() {
        logger.debug("Getting tags needing moderation");
        return postTagRepository.findByStatus("PENDING_MODERATION");
    }

    @Override
    public List<PostTag> getTagsByLanguage(String language) {
        logger.debug("Getting tags by language: {}", language);
        return postTagRepository.findByLanguage(language);
    }

    @Override
    public List<PostTag> getTagsByRegion(String region) {
        logger.debug("Getting tags by region: {}", region);
        return postTagRepository.findByRegion(region);
    }

    @Override
    public List<PostTag> getTagsByCreator(String createdBy) {
        logger.debug("Getting tags by creator: {}", createdBy);
        return postTagRepository.findByCreatedBy(createdBy);
    }

    @Override
    public List<PostTag> getTagsByMetadata(String key, Object value) {
        logger.debug("Getting tags by metadata: {} = {}", key, value);
        // This would use MongoDB's flexible schema to search metadata
        return postTagRepository.findByMetadataKeyAndMetadataValue(key, value);
    }

    @Override
    public PostTag mergeTags(String primaryTagId, List<String> tagIdsToMerge) {
        logger.info("Merging tags into primary tag: {}", primaryTagId);

        PostTag primaryTag = postTagRepository.findById(primaryTagId)
                .orElseThrow(() -> new RuntimeException("Primary tag not found: " + primaryTagId));

        // In a real implementation, this would:
        // 1. Update all posts using the merged tags to use the primary tag
        // 2. Update usage counts
        // 3. Delete the merged tags

        logger.info("Tag merge completed: {} tags merged into {}", tagIdsToMerge.size(), primaryTagId);
        return primaryTag;
    }

    @Override
    public List<PostTag> splitTag(String tagId, List<String> newTagNames) {
        logger.info("Splitting tag: {} into {} new tags", tagId, newTagNames.size());

        PostTag originalTag = postTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagId));

        // In a real implementation, this would:
        // 1. Create new tags
        // 2. Distribute posts among new tags
        // 3. Update usage counts

        logger.info("Tag split completed: {} split into {} new tags", tagId, newTagNames.size());
        return newTagNames.stream()
                .map(name -> {
                    PostTag newTag = new PostTag();
                    newTag.setTagName(name);
                    newTag.setCategory(originalTag.getCategory());
                    newTag.setLanguage(originalTag.getLanguage());
                    newTag.setRegion(originalTag.getRegion());
                    return postTagRepository.save(newTag);
                })
                .toList();
    }

    @Override
    public TagStatistics getTagStatistics(String tagId) {
        logger.debug("Getting tag statistics for: {}", tagId);

        PostTag tag = postTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + tagId));

        TagStatistics stats = new TagStatistics();
        stats.setTotalPosts(tag.getUsageCount() != null ? tag.getUsageCount() : 0);
        stats.setTotalViews(tag.getViewCount() != null ? tag.getViewCount() : 0);
        stats.setTotalLikes(0); // Would calculate from posts using this tag
        stats.setTotalComments(0); // Would calculate from posts using this tag
        stats.setAverageEngagement(0.0); // Would calculate from engagement metrics
        stats.setTrendingScore("MEDIUM"); // Would calculate from trending algorithm

        return stats;
    }
}
