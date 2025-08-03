package com.raved.social.mapper;

import com.raved.social.dto.request.CreateCommentRequest;
import com.raved.social.dto.request.UpdateCommentRequest;
import com.raved.social.dto.response.CommentResponse;
import com.raved.social.model.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for Comment entity and DTOs
 */
@Component
public class CommentMapper {

    public CommentResponse toCommentResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setPostId(comment.getPostId());
        response.setUserId(comment.getUserId());
        response.setParentCommentId(comment.getParentCommentId());
        response.setContent(comment.getContent());
        response.setLikesCount(comment.getLikesCount());
        response.setIsEdited(comment.getIsEdited());
        response.setIsFlagged(comment.getIsFlagged());
        response.setFlaggedReason(comment.getFlaggedReason());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setEditedAt(comment.getEditedAt());

        return response;
    }

    public Comment toComment(CreateCommentRequest request) {
        if (request == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setPostId(request.getPostId());
        comment.setUserId(request.getUserId());
        comment.setParentCommentId(request.getParentCommentId());
        comment.setContent(request.getContent());
        
        // Initialize flags and counters
        comment.setLikesCount(0);
        comment.setIsEdited(false);
        comment.setIsFlagged(false);
        comment.setIsDeleted(false);

        return comment;
    }

    public void updateCommentFromRequest(Comment comment, UpdateCommentRequest request) {
        if (comment == null || request == null) {
            return;
        }

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
            comment.setIsEdited(true);
            comment.setEditedAt(LocalDateTime.now());
        }
    }
}
