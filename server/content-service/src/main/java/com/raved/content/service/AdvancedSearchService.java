package com.raved.content.service;

import com.raved.content.model.elasticsearch.PostSearchDocument;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Advanced search service interface for content search operations
 */
public interface AdvancedSearchService {

    /**
     * Search posts with multiple filters
     *
     * @param query    Search query text
     * @param tags     List of tags to filter by
     * @param category Category to filter by
     * @param status   Status to filter by
     * @param fromDate Start date for date range filter
     * @param toDate   End date for date range filter
     * @param page     Page number (0-based)
     * @param size     Page size
     * @return List of matching posts
     */
    List<PostSearchDocument> searchWithFilters(String query, List<String> tags, 
                                               String category, String status, 
                                               LocalDateTime fromDate, LocalDateTime toDate,
                                               int page, int size);

    /**
     * Find posts similar to a given post
     *
     * @param postId Post ID to find similar posts for
     * @param limit  Maximum number of similar posts to return
     * @return List of similar posts
     */
    List<PostSearchDocument> searchSimilarPosts(String postId, int limit);

    /**
     * Find trending posts based on engagement metrics
     *
     * @param days  Number of days to look back
     * @param limit Maximum number of trending posts to return
     * @return List of trending posts
     */
    List<PostSearchDocument> searchTrendingPosts(int days, int limit);

    /**
     * Get popular tags based on usage frequency
     *
     * @param limit Maximum number of tags to return
     * @return List of popular tags
     */
    List<String> getPopularTags(int limit);

    /**
     * Search posts by author with pagination
     *
     * @param authorId Author ID to search for
     * @param page     Page number (0-based)
     * @param size     Page size
     * @return List of posts by the author
     */
    List<PostSearchDocument> searchByAuthor(String authorId, int page, int size);

    /**
     * Get total count of all posts
     *
     * @return Total post count
     */
    long getTotalPostCount();

    /**
     * Get count of posts by status
     *
     * @param status Post status
     * @return Count of posts with the given status
     */
    long getPostCountByStatus(String status);
}
