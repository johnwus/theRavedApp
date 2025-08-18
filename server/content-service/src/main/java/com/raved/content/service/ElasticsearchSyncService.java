package com.raved.content.service;

import com.raved.content.model.Post;
import com.raved.content.model.elasticsearch.PostSearchDocument;
import com.raved.content.repository.PostRepository;
import com.raved.content.repository.elasticsearch.PostSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for synchronizing data between MongoDB and Elasticsearch Handles
 * real-time indexing and bulk synchronization for content
 */
@Service
public class ElasticsearchSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchSyncService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostSearchRepository postSearchRepository;

    /**
     * Sync a single post to Elasticsearch
     */
    @Async
    public void syncPost(Post post) {
        try {
            PostSearchDocument document = convertToDocument(post);
            postSearchRepository.save(document);
            logger.debug("Synced post {} to Elasticsearch", post.getId());
        } catch (Exception e) {
            logger.error("Failed to sync post {} to Elasticsearch", post.getId(), e);
        }
    }

    /**
     * Bulk sync posts to Elasticsearch
     */
    public void bulkSyncPosts(LocalDateTime since) {
        try {
            logger.info("Starting bulk sync of posts since {}", since);

            List<Post> posts = postRepository.findAll(); // TODO: Add findByUpdatedAtAfter method to PostRepository
            List<PostSearchDocument> documents = posts.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());

            postSearchRepository.saveAll(documents);
            logger.info("Bulk synced {} posts to Elasticsearch", documents.size());
        } catch (Exception e) {
            logger.error("Failed to bulk sync posts", e);
        }
    }

    /**
     * Sync all posts to Elasticsearch
     */
    public void syncAllPosts() {
        try {
            logger.info("Starting full sync of all posts to Elasticsearch");

            List<Post> posts = postRepository.findAll();
            List<PostSearchDocument> documents = posts.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());

            postSearchRepository.saveAll(documents);
            logger.info("Full synced {} posts to Elasticsearch", documents.size());
        } catch (Exception e) {
            logger.error("Failed to full sync posts", e);
        }
    }

    /**
     * Delete post from Elasticsearch
     */
    @Async
    public void deletePost(String postId) {
        try {
            postSearchRepository.deleteById(postId);
            logger.debug("Deleted post {} from Elasticsearch", postId);
        } catch (Exception e) {
            logger.error("Failed to delete post {} from Elasticsearch", postId, e);
        }
    }

    /**
     * Update post in Elasticsearch
     */
    @Async
    public void updatePost(Post post) {
        syncPost(post); // Same as sync for Elasticsearch
    }

    /**
     * Convert Post to PostSearchDocument
     */
    private PostSearchDocument convertToDocument(Post post) {
        PostSearchDocument document = new PostSearchDocument();
        document.setId(post.getId());
        document.setTitle(post.getTitle());
        document.setContent(post.getContent());
        document.setAuthorId(post.getUserId());
        document.setTags(post.getTags());
        document.setStatus(post.getPostType());
        document.setCreatedAt(post.getCreatedAt());
        document.setUpdatedAt(post.getUpdatedAt());
        document.setCategory(post.getCategory());

        // Add metadata
        java.util.Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("visibility", post.getVisibility());
        metadata.put("facultyId", post.getFacultyId());
        metadata.put("likesCount", post.getLikesCount());
        metadata.put("commentsCount", post.getCommentsCount());
        metadata.put("sharesCount", post.getSharesCount());
        metadata.put("engagementScore", post.getEngagementScore());
        metadata.put("trendingScore", post.getTrendingScore());
        metadata.put("viralityScore", post.getViralityScore());
        metadata.put("sentiment", post.getSentiment());
        metadata.put("language", post.getLanguage());
        metadata.put("accessLevel", post.getAccessLevel());
        document.setMetadata(metadata);

        return document;
    }
}
