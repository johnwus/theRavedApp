package com.raved.content.service.impl;

import com.raved.content.dto.response.PostResponse;
import com.raved.content.model.Post;
import com.raved.content.repository.PostRepository;
import com.raved.content.service.ContentModerationService;
import com.raved.content.service.PostService;
import com.raved.content.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ContentModerationService implementation for TheRavedApp MongoDB
 */
@Service
public class ContentModerationServiceImpl implements ContentModerationService {

    private static final Logger logger = LoggerFactory.getLogger(ContentModerationServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Override
    public void moderatePost(String postId, String status, String reason, String moderatorId) {
        logger.info("Moderating post: {} to status: {} by moderator: {}", postId, status, moderatorId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        post.setModerationStatus(status);
        post.setModerationReason(reason);
        post.setModeratorId(moderatorId);
        post.setModeratedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        // Update moderation flags
        if ("REJECTED".equals(status)) {
            post.setIsFlagged(true);
            post.setFlaggedReason(reason);
        } else if ("APPROVED".equals(status)) {
            post.setIsFlagged(false);
            post.setFlaggedReason(null);
        }

        postRepository.save(post);
        logger.info("Post moderated successfully: {} to {}", postId, status);
    }

    @Override
    public Page<PostResponse> getPostsRequiringModeration(Pageable pageable) {
        logger.debug("Getting posts requiring moderation");
        return postRepository.findByModerationStatus("PENDING", pageable)
                .map(postMapper::toPostResponse);
    }

    @Override
    public String autoModerateContent(String content) {
        logger.debug("Auto-moderating content");

        // Simple content moderation rules (in production, this would use AI/ML)
        String lowerContent = content.toLowerCase();

        if (lowerContent.contains("spam") || lowerContent.contains("scam")) {
            return "REJECTED";
        } else if (lowerContent.contains("inappropriate") || lowerContent.contains("offensive")) {
            return "FLAGGED";
        } else if (lowerContent.length() < 10) {
            return "FLAGGED";
        } else {
            return "APPROVED";
        }
    }

    @Override
    public void flagContent(String contentId, String contentType, String reason, String reporterId) {
        logger.info("Flagging content: {} of type: {} for reason: {} by reporter: {}",
                contentId, contentType, reason, reporterId);

        if ("POST".equals(contentType)) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("Post not found: " + contentId));

            post.setIsFlagged(true);
            post.setFlaggedReason(reason);
            post.setModerationStatus("FLAGGED");
            post.setUpdatedAt(LocalDateTime.now());

            postRepository.save(post);
            logger.info("Post flagged successfully: {}", contentId);
        } else {
            logger.warn("Unsupported content type for flagging: {}", contentType);
        }
    }

    @Override
    public List<PostResponse> getFlaggedContent() {
        logger.debug("Getting flagged content");
        return postRepository.findByIsFlaggedTrue()
                .stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void approveContent(String contentId, String contentType) {
        logger.info("Approving content: {} of type: {}", contentId, contentType);

        if ("POST".equals(contentType)) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("Post not found: " + contentId));

            post.setModerationStatus("APPROVED");
            post.setIsFlagged(false);
            post.setFlaggedReason(null);
            post.setModeratedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            postRepository.save(post);
            logger.info("Post approved successfully: {}", contentId);
        } else {
            logger.warn("Unsupported content type for approval: {}", contentType);
        }
    }

    @Override
    public void rejectContent(String contentId, String contentType, String reason) {
        logger.info("Rejecting content: {} of type: {} for reason: {}", contentId, contentType, reason);

        if ("POST".equals(contentType)) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("Post not found: " + contentId));

            post.setModerationStatus("REJECTED");
            post.setModerationReason(reason);
            post.setModeratedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            postRepository.save(post);
            logger.info("Post rejected successfully: {}", contentId);
        } else {
            logger.warn("Unsupported content type for rejection: {}", contentType);
        }
    }

    @Override
    public ModerationStats getModerationStats() {
        logger.debug("Getting moderation statistics");

        ModerationStats stats = new ModerationStats();

        // Count posts by moderation status
        stats.setPendingCount((int) postRepository.countByModerationStatus("PENDING"));
        stats.setApprovedCount((int) postRepository.countByModerationStatus("APPROVED"));
        stats.setRejectedCount((int) postRepository.countByModerationStatus("REJECTED"));
        stats.setFlaggedCount((int) postRepository.countByIsFlaggedTrue());

        logger.debug("Moderation stats - Pending: {}, Approved: {}, Rejected: {}, Flagged: {}",
                stats.getPendingCount(), stats.getApprovedCount(),
                stats.getRejectedCount(), stats.getFlaggedCount());

        return stats;
    }
}
