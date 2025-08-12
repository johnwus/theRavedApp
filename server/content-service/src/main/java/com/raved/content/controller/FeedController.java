package com.raved.content.controller;

import com.raved.content.dto.response.FeedResponse;
import com.raved.content.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for feed operations MongoDB
 */
@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    @Autowired
    private FeedService feedService;

    /**
     * Get personalized feed for a user
     */
    @GetMapping("/personalized/{userId}")
    public ResponseEntity<FeedResponse> getPersonalizedFeed(@PathVariable String userId, Pageable pageable) {
        var feed = feedService.getPersonalizedFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get discover feed for a user
     */
    @GetMapping("/discover/{userId}")
    public ResponseEntity<FeedResponse> getDiscoverFeed(@PathVariable String userId, Pageable pageable) {
        var feed = feedService.getDiscoverFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get trending feed
     */
    @GetMapping("/trending")
    public ResponseEntity<FeedResponse> getTrendingFeed(Pageable pageable) {
        var feed = feedService.getTrendingFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get university feed
     */
    @GetMapping("/university/{userId}/{universityId}")
    public ResponseEntity<FeedResponse> getUniversityFeed(@PathVariable String userId,
            @PathVariable String universityId,
            Pageable pageable) {
        var feed = feedService.getUniversityFeed(userId, universityId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get faculty feed
     */
    @GetMapping("/faculty/{userId}/{facultyId}")
    public ResponseEntity<FeedResponse> getFacultyFeed(@PathVariable String userId,
            @PathVariable String facultyId,
            Pageable pageable) {
        var feed = feedService.getFacultyFeed(userId, facultyId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get hashtag feed
     */
    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<FeedResponse> getHashtagFeed(@PathVariable String hashtag, Pageable pageable) {
        var feed = feedService.getHashtagFeed(hashtag, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get recent feed
     */
    @GetMapping("/recent/{userId}")
    public ResponseEntity<FeedResponse> getRecentFeed(@PathVariable String userId, Pageable pageable) {
        var feed = feedService.getRecentFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get popular feed
     */
    @GetMapping("/popular")
    public ResponseEntity<FeedResponse> getPopularFeed(Pageable pageable) {
        var feed = feedService.getPopularFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get category feed
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<FeedResponse> getCategoryFeed(@PathVariable String category, Pageable pageable) {
        var feed = feedService.getCategoryFeed(category, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get language-specific feed
     */
    @GetMapping("/language/{language}")
    public ResponseEntity<FeedResponse> getLanguageFeed(@PathVariable String language, Pageable pageable) {
        var feed = feedService.getLanguageFeed(language, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get sentiment-based feed
     */
    @GetMapping("/sentiment/{sentiment}")
    public ResponseEntity<FeedResponse> getSentimentFeed(@PathVariable String sentiment, Pageable pageable) {
        var feed = feedService.getSentimentFeed(sentiment, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get engagement-based feed
     */
    @GetMapping("/engagement")
    public ResponseEntity<FeedResponse> getEngagementFeed(@RequestParam double minScore, Pageable pageable) {
        var feed = feedService.getEngagementFeed(minScore, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get mixed feed
     */
    @GetMapping("/mixed/{userId}")
    public ResponseEntity<FeedResponse> getMixedFeed(@PathVariable String userId, Pageable pageable) {
        var feed = feedService.getMixedFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get scheduled posts feed
     */
    @GetMapping("/scheduled")
    public ResponseEntity<FeedResponse> getScheduledFeed(Pageable pageable) {
        var feed = feedService.getScheduledFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get featured posts feed
     */
    @GetMapping("/featured")
    public ResponseEntity<FeedResponse> getFeaturedFeed(Pageable pageable) {
        var feed = feedService.getFeaturedFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Get pinned posts feed
     */
    @GetMapping("/pinned")
    public ResponseEntity<FeedResponse> getPinnedFeed(Pageable pageable) {
        var feed = feedService.getPinnedFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    /**
     * Refresh user feed cache
     */
    @PostMapping("/refresh/{userId}")
    public ResponseEntity<Void> refreshUserFeed(@PathVariable String userId) {
        feedService.refreshUserFeed(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Refresh global feeds cache
     */
    @PostMapping("/refresh/global")
    public ResponseEntity<Void> refreshGlobalFeeds() {
        feedService.refreshGlobalFeeds();
        return ResponseEntity.ok().build();
    }
}
