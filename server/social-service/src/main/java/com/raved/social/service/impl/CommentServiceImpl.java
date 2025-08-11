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
        // The Comment entity has @PrePersist and @PreUpdate annotations, so timestamps are handled automatically
        
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created successfully with ID: {}", savedComment.getId());
        
        return commentMapper.toCommentResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentResponse> getCommentById(Long id) {
        logger.debug("Getting comment by ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if (commentOpt.isPresent() && commentOpt.get().getIsDeleted()) {
            return Optional.empty();
        }
        return commentOpt.map(commentMapper::toCommentResponse);
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest request) {
        logger.info("Updating comment with ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("Comment not found with ID: " + id);
        }
        
        Comment comment = commentOpt.get();
        
        // Check if comment is deleted
        if (comment.getIsDeleted()) {
            throw new CommentNotFoundException("Comment is deleted");
        }
        
        // Note: This method doesn't have userId parameter, so we can't check authorization
        // In a real implementation, you'd want to get the current user from security context
        
        commentMapper.updateCommentFromRequest(comment, request);
        // The Comment entity has @PreUpdate annotation, so updatedAt is handled automatically
        
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment updated successfully with ID: {}", id);
        
        return commentMapper.toCommentResponse(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("Comment not found with ID: " + id);
        }
        
        Comment comment = commentOpt.get();
        
        // Check if comment is deleted
        if (comment.getIsDeleted()) {
            throw new CommentNotFoundException("Comment is already deleted");
        }
        
        // Note: This method doesn't have userId parameter, so we can't check authorization
        // In a real implementation, you'd want to get the current user from security context
        
        commentRepository.softDeleteById(id);
        logger.info("Comment deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        logger.debug("Getting comments for post ID: {}", postId);
        
        Page<Comment> comments = commentRepository.findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(postId, pageable);
        return comments.map(commentMapper::toCommentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByUser(Long userId, Pageable pageable) {
        logger.debug("Getting comments for user ID: {}", userId);
        
        Page<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return comments.map(commentMapper::toCommentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentReplies(Long parentCommentId) {
        logger.debug("Getting replies for comment ID: {}", parentCommentId);
        
        List<Comment> replies = commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentCommentId);
        return replies.stream()
                .filter(comment -> !comment.getIsDeleted())
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long commentId, Long userId) {
        logger.info("User {} liking comment {}", userId, commentId);
        
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent() && !commentOpt.get().getIsDeleted()) {
            Comment comment = commentOpt.get();
            
            // For now, we'll use a simple approach. In a real implementation,
            // you'd want to check if the user already liked this comment
            // using a separate CommentLike entity or similar mechanism
            
            commentRepository.incrementLikesCount(commentId);
            logger.info("Comment liked successfully");
        } else {
            logger.warn("Comment not found or is deleted");
        }
    }

    @Override
    public void unlikeComment(Long commentId, Long userId) {
        logger.info("User {} unliking comment {}", userId, commentId);
        
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent() && !commentOpt.get().getIsDeleted()) {
            Comment comment = commentOpt.get();
            
            // For now, we'll use a simple approach. In a real implementation,
            // you'd want to check if the user has liked this comment
            // using a separate CommentLike entity or similar mechanism
            
            commentRepository.decrementLikesCount(commentId);
            logger.info("Comment unliked successfully");
        } else {
            logger.warn("Comment not found or is deleted");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getCommentLikeCount(Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent() && !commentOpt.get().getIsDeleted()) {
            return commentOpt.get().getLikesCount();
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedComment(Long commentId, Long userId) {
        // This would need to be implemented with a separate CommentLike entity
        // For now, return false as a placeholder
        logger.warn("hasUserLikedComment not implemented - requires CommentLike entity");
        return false;
    }

    @Override
    public void reportComment(Long commentId, Long reporterId, String reason) {
        logger.info("Reporting comment ID: {} by user: {} for reason: {}", commentId, reporterId, reason);
        
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent() && !commentOpt.get().getIsDeleted()) {
            // Note: flaggedReason is not stored in the database, only isFlagged is set
            // In a real implementation, you might want to store reports in a separate table
            commentRepository.flagComment(commentId, reason);
            logger.info("Comment reported successfully");
        } else {
            logger.warn("Comment not found or is deleted");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getRecentCommentsForUser(Long userId, int limit) {
        logger.debug("Getting recent comments for user: {} with limit: {}", userId, limit);
        
        // This method would need a custom repository method
        // For now, we'll get user's comments and limit them
        Pageable pageable = Pageable.ofSize(limit);
        Page<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return comments.getContent().stream()
                .filter(comment -> !comment.getIsDeleted())
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }
}
