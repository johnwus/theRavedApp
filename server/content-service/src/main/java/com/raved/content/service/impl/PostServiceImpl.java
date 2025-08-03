package com.raved.content.service.impl;

import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.dto.request.UpdatePostRequest;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.exception.PostNotFoundException;
import com.raved.content.mapper.PostMapper;
import com.raved.content.model.Post;
import com.raved.content.repository.PostRepository;
import com.raved.content.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PostService
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Override
    public PostResponse createPost(CreatePostRequest request) {
        logger.info("Creating new post for user: {}", request.getUserId());
        
        Post post = postMapper.toPost(request);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        logger.info("Post created successfully with ID: {}", savedPost.getId());
        
        return postMapper.toPostResponse(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostResponse> getPostById(Long id) {
        logger.debug("Getting post by ID: {}", id);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(id);
        return postOpt.map(postMapper::toPostResponse);
    }

    @Override
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        logger.info("Updating post with ID: {}", id);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("Post not found with ID: " + id);
        }
        
        Post post = postOpt.get();
        
        // Update fields
        if (request.getContent() != null) {
            post.setContent(request.getContent());
            post.setIsEdited(true);
            post.setEditedAt(LocalDateTime.now());
        }
        if (request.getVisibility() != null) {
            post.setVisibility(Post.Visibility.valueOf(request.getVisibility()));
        }
        if (request.getAllowComments() != null) {
            post.setAllowComments(request.getAllowComments());
        }
        if (request.getAllowSharing() != null) {
            post.setAllowSharing(request.getAllowSharing());
        }
        
        post.setUpdatedAt(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        logger.info("Post updated successfully with ID: {}", id);
        
        return postMapper.toPostResponse(savedPost);
    }

    @Override
    public void deletePost(Long id) {
        logger.info("Deleting post with ID: {}", id);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("Post not found with ID: " + id);
        }
        
        Post post = postOpt.get();
        post.setIsDeleted(true);
        post.setUpdatedAt(LocalDateTime.now());
        
        postRepository.save(post);
        logger.info("Post deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByUserId(Long userId, Pageable pageable) {
        logger.debug("Getting posts for user ID: {}", userId);
        
        Page<Post> posts = postRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId, pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByFacultyId(Long facultyId, Pageable pageable) {
        logger.debug("Getting posts for faculty ID: {}", facultyId);
        
        Page<Post> posts = postRepository.findByFacultyIdAndIsDeletedFalseOrderByCreatedAtDesc(facultyId, pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPublicPosts(Pageable pageable) {
        logger.debug("Getting public posts");
        
        Page<Post> posts = postRepository.findByVisibilityAndIsDeletedFalseOrderByCreatedAtDesc(
                Post.Visibility.PUBLIC, pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getTrendingPosts(int limit) {
        logger.debug("Getting trending posts with limit: {}", limit);
        
        List<Post> posts = postRepository.findTrendingPosts(limit);
        return posts.stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getFeaturedPosts(int limit) {
        logger.debug("Getting featured posts with limit: {}", limit);
        
        List<Post> posts = postRepository.findByIsFeaturedTrueAndIsDeletedFalseAndFeaturedUntilAfterOrderByCreatedAtDesc(
                LocalDateTime.now()).stream().limit(limit).collect(Collectors.toList());
        return posts.stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(String query, Pageable pageable) {
        logger.debug("Searching posts with query: {}", query);
        
        Page<Post> posts = postRepository.searchPosts(query, pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    public void incrementViewCount(Long postId) {
        logger.debug("Incrementing view count for post ID: {}", postId);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setViewsCount(post.getViewsCount() + 1);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void updateEngagementMetrics(Long postId, int likesCount, int commentsCount, int sharesCount) {
        logger.debug("Updating engagement metrics for post ID: {}", postId);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setLikesCount(likesCount);
            post.setCommentsCount(commentsCount);
            post.setSharesCount(sharesCount);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void flagPost(Long postId, String reason) {
        logger.info("Flagging post ID: {} with reason: {}", postId, reason);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsFlagged(true);
            post.setFlaggedReason(reason);
            post.setModerationStatus(Post.ModerationStatus.PENDING);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
            
            logger.info("Post flagged successfully: {}", postId);
        }
    }

    @Override
    public void featurePost(Long postId, int durationHours) {
        logger.info("Featuring post ID: {} for {} hours", postId, durationHours);
        
        Optional<Post> postOpt = postRepository.findByIdAndIsDeletedFalse(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsFeatured(true);
            post.setFeaturedUntil(LocalDateTime.now().plusHours(durationHours));
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
            
            logger.info("Post featured successfully: {}", postId);
        }
    }
}
