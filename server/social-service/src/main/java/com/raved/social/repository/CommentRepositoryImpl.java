package com.raved.social.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.raved.social.model.Comment;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CommentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementLikesCount(String commentId) {
        Query query = Query.query(Criteria.where("_id").is(commentId).and("isDeleted").is(false));
        Update update = new Update().inc("likesCount", 1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

    @Override
    public void decrementLikesCount(String commentId) {
        Query query = Query.query(Criteria.where("_id").is(commentId).and("isDeleted").is(false));
        Update update = new Update().inc("likesCount", -1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

    @Override
    public void flagComment(String commentId, String reason) {
        Query query = Query.query(Criteria.where("_id").is(commentId).and("isDeleted").is(false));
        Update update = new Update()
                .set("isFlagged", true)
                .set("moderationReason", reason);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }
}

