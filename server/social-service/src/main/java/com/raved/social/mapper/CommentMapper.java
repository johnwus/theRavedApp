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
        response.setRepliesCount(comment.getRepliesCount());
        response.setIsFlagged(comment.getIsFlagged());
        response.setModerationStatus(comment.getModerationStatus());
        response.setIsDeleted(comment.getIsDeleted());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        
        // Set default values for additional fields
        // These would typically be populated by the service layer
        response.setUserName(null); // To be set by service
        response.setUserAvatar(null); // To be set by service
        response.setIsLikedByCurrentUser(false); // To be set by service
        response.setFlaggedReason(null); // Not stored in database, to be set by service if needed

        return response;
    }

    public CommentResponse toCommentResponse(Comment comment, String userName, String userAvatar, Boolean isLikedByCurrentUser) {
        CommentResponse response = toCommentResponse(comment);
        if (response != null) {
            response.setUserName(userName);
            response.setUserAvatar(userAvatar);
            response.setIsLikedByCurrentUser(isLikedByCurrentUser != null ? isLikedByCurrentUser : false);
            // flaggedReason is not stored in database, so it remains null unless set by service
        }
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
        comment.setRepliesCount(0);
        comment.setIsFlagged(false);
        comment.setModerationStatus("APPROVED");
        comment.setIsDeleted(false);

        return comment;
    }

    public void updateCommentFromRequest(Comment comment, UpdateCommentRequest request) {
        if (comment == null || request == null) {
            return;
        }

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }
    }
}