package com.raved.content.service;

import com.raved.content.dto.response.PostTagResponse;
import com.raved.content.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * TagService for TheRavedApp MongoDB
 */
public interface TagService {

    /**
     * Create a new tag
     */
    PostTag createTag(PostTag tag);

    /**
     * Get tag by ID
     */
    Optional<PostTag> getTagById(String id);

    /**
     * Get tag by name
     */
    Optional<PostTag> getTagByName(String name);

    /**
     * Update tag
     */
    PostTag updateTag(String id, PostTag tag);

    /**
     * Delete tag
     */
    void deleteTag(String id);

    /**
     * Get all tags with pagination
     */
    Page<PostTag> getAllTags(Pageable pageable);

    /**
     * Get tags by category
     */
    List<PostTag> getTagsByCategory(String category);

    /**
     * Get tags by status
     */
    List<PostTag> getTagsByStatus(String status);

    /**
     * Get trending tags
     */
    List<PostTag> getTrendingTags(int limit);

    /**
     * Get popular tags
     */
    List<PostTag> getPopularTags(int limit);

    /**
     * Search tags by name
     */
    List<PostTag> searchTags(String query, int limit);

    /**
     * Get related tags
     */
    List<PostTag> getRelatedTags(String tagName, int limit);

    /**
     * Get parent tags
     */
    List<PostTag> getParentTags(String tagName);

    /**
     * Get child tags
     */
    List<PostTag> getChildTags(String tagName);

    /**
     * Increment tag usage count
     */
    void incrementTagUsage(String tagName);

    /**
     * Increment tag view count
     */
    void incrementTagView(String tagName);

    /**
     * Increment tag click count
     */
    void incrementTagClick(String tagName);

    /**
     * Increment tag search count
     */
    void incrementTagSearch(String tagName);

    /**
     * Moderate tag
     */
    void moderateTag(String tagId, String status, String reason, String moderatorId);

    /**
     * Get tags needing moderation
     */
    List<PostTag> getTagsNeedingModeration();

    /**
     * Get tags by language
     */
    List<PostTag> getTagsByLanguage(String language);

    /**
     * Get tags by region
     */
    List<PostTag> getTagsByRegion(String region);

    /**
     * Get tags by creator
     */
    List<PostTag> getTagsByCreator(String createdBy);

    /**
     * Get tags by metadata
     */
    List<PostTag> getTagsByMetadata(String key, Object value);

    /**
     * Merge tags
     */
    PostTag mergeTags(String primaryTagId, List<String> tagIdsToMerge);

    /**
     * Split tag
     */
    List<PostTag> splitTag(String tagId, List<String> newTagNames);

    /**
     * Get tag statistics
     */
    TagStatistics getTagStatistics(String tagId);

    /**
     * Tag statistics class
     */
    class TagStatistics {
        private int totalPosts;
        private int totalViews;
        private int totalLikes;
        private int totalComments;
        private double averageEngagement;
        private String trendingScore;

        // Getters and setters
        public int getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(int totalPosts) {
            this.totalPosts = totalPosts;
        }

        public int getTotalViews() {
            return totalViews;
        }

        public void setTotalViews(int totalViews) {
            this.totalViews = totalViews;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(int totalLikes) {
            this.totalLikes = totalLikes;
        }

        public int getTotalComments() {
            return totalComments;
        }

        public void setTotalComments(int totalComments) {
            this.totalComments = totalComments;
        }

        public double getAverageEngagement() {
            return averageEngagement;
        }

        public void setAverageEngagement(double averageEngagement) {
            this.averageEngagement = averageEngagement;
        }

        public String getTrendingScore() {
            return trendingScore;
        }

        public void setTrendingScore(String trendingScore) {
            this.trendingScore = trendingScore;
        }
    }
}
