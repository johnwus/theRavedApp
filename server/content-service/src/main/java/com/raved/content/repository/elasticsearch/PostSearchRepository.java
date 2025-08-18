package com.raved.content.repository.elasticsearch;

import com.raved.content.model.elasticsearch.PostSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch repository for Post search documents
 * Provides methods for full-text search and analytics
 */
@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostSearchDocument, String> {

    /**
     * Find posts by title containing the given text
     */
    List<PostSearchDocument> findByTitleContaining(String title);

    /**
     * Find posts by content containing the given text
     */
    List<PostSearchDocument> findByContentContaining(String content);

    /**
     * Find posts by author ID
     */
    List<PostSearchDocument> findByAuthorId(String authorId);

    /**
     * Find posts by status
     */
    List<PostSearchDocument> findByStatus(String status);

    /**
     * Find posts by tags
     */
    List<PostSearchDocument> findByTagsIn(List<String> tags);

    /**
     * Find posts by category
     */
    List<PostSearchDocument> findByCategory(String category);

    /**
     * Find posts created after the given date
     */
    List<PostSearchDocument> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find posts by author ID and status
     */
    List<PostSearchDocument> findByAuthorIdAndStatus(String authorId, String status);

    /**
     * Custom query for full-text search across title and content
     */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"tags^1\"]}}")
    List<PostSearchDocument> findByFullTextSearch(String searchText);

    /**
     * Find posts by multiple criteria with text search
     */
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\"]}}, {\"term\": {\"status\": \"?1\"}}, {\"term\": {\"category\": \"?2\"}}]}}")
    List<PostSearchDocument> findByTextAndStatusAndCategory(String searchText, String status, String category);
}
