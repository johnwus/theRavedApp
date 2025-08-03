package com.raved.analytics.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for trending topics
 */
public class TrendingTopicResponse {

    private String topic;
    private String hashtag;
    private Long mentionCount;
    private Long uniqueUsers;
    private Double trendingScore;
    private Integer trendingRank;
    private String category;
    private LocalDateTime trendingSince;
    private LocalDateTime lastMentionAt;

    // Constructors
    public TrendingTopicResponse() {}

    public TrendingTopicResponse(String topic, Long mentionCount, Double trendingScore) {
        this.topic = topic;
        this.mentionCount = mentionCount;
        this.trendingScore = trendingScore;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Long getMentionCount() {
        return mentionCount;
    }

    public void setMentionCount(Long mentionCount) {
        this.mentionCount = mentionCount;
    }

    public Long getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Long uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Double getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(Double trendingScore) {
        this.trendingScore = trendingScore;
    }

    public Integer getTrendingRank() {
        return trendingRank;
    }

    public void setTrendingRank(Integer trendingRank) {
        this.trendingRank = trendingRank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getTrendingSince() {
        return trendingSince;
    }

    public void setTrendingSince(LocalDateTime trendingSince) {
        this.trendingSince = trendingSince;
    }

    public LocalDateTime getLastMentionAt() {
        return lastMentionAt;
    }

    public void setLastMentionAt(LocalDateTime lastMentionAt) {
        this.lastMentionAt = lastMentionAt;
    }
}
