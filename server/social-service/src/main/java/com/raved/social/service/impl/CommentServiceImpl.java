package com.raved.social.service.impl;

import com.raved.social.dto.request.CreateCommentRequest;
import com.raved.social.dto.request.UpdateCommentRequest;
import com.raved.social.dto.response.CommentResponse;
import com.raved.social.exception.CommentNotFoundException;
import com.raved.social.exception.UnauthorizedAccessException;
import com.raved.social.mapper.CommentMapper;
import com.raved.social.model.Comment;
import com.raved.social.repository.CommentRepository;
import com.raved.social.service.CommentService;
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
 * Implementation of CommentService
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CreateCommentRequest request) {
        logger.info("Creating new comment for post: {} by user: {}", request.getPostId(), request.getUserId());
        
        Comment comment = commentMapper.toComment(request);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setIsDeleted(false);
        comment.setLikesCount(0);
        
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created successfully with ID: {}", savedComment.getId());
        
        return commentMapper.toCommentResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentResponse> getCommentById(Long id) {
        logger.debug("Getting comment by ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(id);
        return commentOpt.map(commentMapper::toCommentResponse);
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest request) {
        logger.info("Updating comment with ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(id);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("Comment not found with ID: " + id);
        }
        
        Comment comment = commentOpt.get();
        
        // Check if user is authorized to update this comment
        if (!comment.getUserId().equals(request.getUserId())) {
            throw new UnauthorizedAccessException("User not authorized to update this comment");
        }
        
        comment.setContent(request.getContent());
        comment.setIsEdited(true);
        comment.setEditedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment updated successfully with ID: {}", id);
        
        return commentMapper.toCommentResponse(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(id);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("Comment not found with ID: " + id);
        }
        
        Comment comment = commentOpt.get();
        comment.setIsDeleted(true);
        comment.setUpdatedAt(LocalDateTime.now());
        
        commentRepository.save(comment);
        logger.info("Comment deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        logger.debug("Getting comments for post ID: {}", postId);
        
        Page<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId, pageable);
        return comments.map(commentMapper::toCommentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByUser(Long userId, Pageable pageable) {
        logger.debug("Getting comments for user ID: {}", userId);
        
        Page<Comment> comments = commentRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId, pageable);
        return comments.map(commentMapper::toCommentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentReplies(Long parentCommentId) {
        logger.debug("Getting replies for comment ID: {}", parentCommentId);
        
        List<Comment> replies = commentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(parentCommentId);
        return replies.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long commentId, Long userId) {
        logger.info("User {} liking comment {}", userId, commentId);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // Check if user already liked this comment
            if (!commentRepository.hasUserLikedComment(commentId, userId)) {
                // Add like logic here (could be a separate CommentLike entity)
                comment.setLikesCount(comment.getLikesCount() + 1);
                comment.setUpdatedAt(LocalDateTime.now());
                commentRepository.save(comment);
                
                logger.info("Comment liked successfully");
            } else {
                logger.warn("User has already liked this comment");
            }
        }
    }

    @Override
    public void unlikeComment(Long commentId, Long userId) {
        logger.info("User {} unliking comment {}", userId, commentId);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            // Check if user has liked this comment
            if (commentRepository.hasUserLikedComment(commentId, userId)) {
                // Remove like logic here
                comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
                comment.setUpdatedAt(LocalDateTime.now());
                commentRepository.save(comment);
                
                logger.info("Comment unliked successfully");
            } else {
                logger.warn("User has not liked this comment");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getCommentLikeCount(Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(commentId);
        return commentOpt.map(Comment::getLikesCount).orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedComment(Long commentId, Long userId) {
        return commentRepository.hasUserLikedComment(commentId, userId);
    }

    @Override
    public void reportComment(Long commentId, Long reporterId, String reason) {
        logger.info("Reporting comment ID: {} by user: {} for reason: {}", commentId, reporterId, reason);
        
        Optional<Comment> commentOpt = commentRepository.findByIdAndIsDeletedFalse(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            comment.setIsFlagged(true);
            comment.setFlaggedReason(reason);
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);
            
            logger.info("Comment reported successfully");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getRecentCommentsForUser(Long userId, int limit) {
        logger.debug("Getting recent comments for user: {} with limit: {}", userId, limit);
        
        List<Comment> comments = commentRepository.findRecentCommentsForUserPosts(userId, limit);
        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }
}
