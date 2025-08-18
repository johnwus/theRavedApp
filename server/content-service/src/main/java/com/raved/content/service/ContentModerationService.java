package com.raved.content.service;

import com.raved.content.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ContentModerationService for TheRavedApp MongoDB
 */
public interface ContentModerationService {

    /**
     * Moderate post
     */
    void moderatePost(String postId, String status, String reason, String moderatorId);

    /**
     * Get posts requiring moderation
     */
    Page<PostResponse> getPostsRequiringModeration(Pageable pageable);

    /**
     * Auto-moderate content using AI
     */
    String autoModerateContent(String content);

    /**
     * Flag content for review
     */
    void flagContent(String contentId, String contentType, String reason, String reporterId);

    /**
     * Get flagged content
     */
    List<PostResponse> getFlaggedContent();

    /**
     * Approve content
     */
    void approveContent(String contentId, String contentType);

    /**
     * Reject content
     */
    void rejectContent(String contentId, String contentType, String reason);

    /**
     * Get moderation statistics
     */
    ModerationStats getModerationStats();

    /**
     * Moderation statistics class
     */
    class ModerationStats {
        private int pendingCount;
        private int approvedCount;
        private int rejectedCount;
        private int flaggedCount;

        // Getters and setters
        public int getPendingCount() {
            return pendingCount;
        }

        public void setPendingCount(int pendingCount) {
            this.pendingCount = pendingCount;
        }

        public int getApprovedCount() {
            return approvedCount;
        }

        public void setApprovedCount(int approvedCount) {
            this.approvedCount = approvedCount;
        }

        public int getRejectedCount() {
            return rejectedCount;
        }

        public void setRejectedCount(int rejectedCount) {
            this.rejectedCount = rejectedCount;
        }

        public int getFlaggedCount() {
            return flaggedCount;
        }

        public void setFlaggedCount(int flaggedCount) {
            this.flaggedCount = flaggedCount;
        }
    }
}
