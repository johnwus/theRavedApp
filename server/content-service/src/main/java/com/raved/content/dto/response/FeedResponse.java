package com.raved.content.dto.response;

import java.util.List;

/**
 * FeedResponse for TheRavedApp
 */
public class FeedResponse {
    private List<PostResponse> posts;
    private int totalElements;
    private int currentPage;

    public FeedResponse() {
    }

    public FeedResponse(List<PostResponse> posts, int totalElements, int currentPage) {
        this.posts = posts;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
