package com.raved.social.repository;

public interface CommentRepositoryCustom {
    void incrementLikesCount(String commentId);
    void decrementLikesCount(String commentId);
    void flagComment(String commentId, String reason);
}

