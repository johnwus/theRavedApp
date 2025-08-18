package com.raved.content.service;

import com.raved.content.dto.request.MediaUploadRequest;
import com.raved.content.dto.response.MediaResponse;
import com.raved.content.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * MediaService for TheRavedApp MongoDB
 */
public interface MediaService {

    /**
     * Upload media file
     */
    MediaResponse uploadMedia(MultipartFile file, MediaUploadRequest request);

    /**
     * Get media by ID
     */
    Optional<MediaResponse> getMediaById(String id);

    /**
     * Update media metadata
     */
    MediaResponse updateMedia(String id, MediaUploadRequest request);

    /**
     * Delete media
     */
    void deleteMedia(String id);

    /**
     * Get media by post ID
     */
    List<MediaResponse> getMediaByPostId(String postId);

    /**
     * Get media by uploader
     */
    Page<MediaResponse> getMediaByUploader(String uploaderId, Pageable pageable);

    /**
     * Get media by type
     */
    List<MediaResponse> getMediaByType(String mediaType);

    /**
     * Get media by status
     */
    List<MediaResponse> getMediaByStatus(String status);

    /**
     * Get trending media
     */
    List<MediaResponse> getTrendingMedia(int limit);

    /**
     * Get recent media
     */
    List<MediaResponse> getRecentMedia(int limit);

    /**
     * Search media by filename
     */
    List<MediaResponse> searchMedia(String query, int limit);

    /**
     * Process media file
     */
    void processMedia(String mediaId);

    /**
     * Generate thumbnail
     */
    String generateThumbnail(String mediaId);

    /**
     * Update media status
     */
    void updateMediaStatus(String mediaId, String status);

    /**
     * Update processing status
     */
    void updateProcessingStatus(String mediaId, String processingStatus);

    /**
     * Get media statistics
     */
    MediaStatistics getMediaStatistics(String mediaId);

    /**
     * Increment view count
     */
    void incrementViewCount(String mediaId);

    /**
     * Increment download count
     */
    void incrementDownloadCount(String mediaId);

    /**
     * Increment share count
     */
    void incrementShareCount(String mediaId);

    /**
     * Get media by dimensions
     */
    List<MediaResponse> getMediaByDimensions(Integer width, Integer height);

    /**
     * Get media by duration range
     */
    List<MediaResponse> getMediaByDuration(Integer minDuration, Integer maxDuration);

    /**
     * Get media by file size range
     */
    List<MediaResponse> getMediaByFileSize(Long minSize, Long maxSize);

    /**
     * Get media by MIME type
     */
    List<MediaResponse> getMediaByMimeType(String mimeType);

    /**
     * Get media by access level
     */
    List<MediaResponse> getMediaByAccessLevel(String accessLevel);

    /**
     * Get public media
     */
    List<MediaResponse> getPublicMedia(int limit);

    /**
     * Get media by language
     */
    List<MediaResponse> getMediaByLanguage(String language);

    /**
     * Get media by content hash
     */
    Optional<MediaResponse> getMediaByContentHash(String contentHash);

    /**
     * Get pending processing media
     */
    List<MediaResponse> getPendingProcessingMedia();

    /**
     * Get failed media
     */
    List<MediaResponse> getFailedMedia();

    /**
     * Retry failed media processing
     */
    void retryMediaProcessing(String mediaId);

    /**
     * Get media analytics
     */
    MediaAnalytics getMediaAnalytics(String mediaId);

    /**
     * Media statistics class
     */
    class MediaStatistics {
        private int totalViews;
        private int totalDownloads;
        private int totalShares;
        private double averageRating;
        private String popularityScore;

        // Getters and setters
        public int getTotalViews() {
            return totalViews;
        }

        public void setTotalViews(int totalViews) {
            this.totalViews = totalViews;
        }

        public int getTotalDownloads() {
            return totalDownloads;
        }

        public void setTotalDownloads(int totalDownloads) {
            this.totalDownloads = totalDownloads;
        }

        public int getTotalShares() {
            return totalShares;
        }

        public void setTotalShares(int totalShares) {
            this.totalShares = totalShares;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }

        public String getPopularityScore() {
            return popularityScore;
        }

        public void setPopularityScore(String popularityScore) {
            this.popularityScore = popularityScore;
        }
    }

    /**
     * Media analytics class
     */
    class MediaAnalytics {
        private String mediaId;
        private String viewTrend;
        private String downloadTrend;
        private String shareTrend;
        private String engagementScore;
        private String viralityScore;

        // Getters and setters
        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getViewTrend() {
            return viewTrend;
        }

        public void setViewTrend(String viewTrend) {
            this.viewTrend = viewTrend;
        }

        public String getDownloadTrend() {
            return downloadTrend;
        }

        public void setDownloadTrend(String downloadTrend) {
            this.downloadTrend = downloadTrend;
        }

        public String getShareTrend() {
            return shareTrend;
        }

        public void setShareTrend(String shareTrend) {
            this.shareTrend = shareTrend;
        }

        public String getEngagementScore() {
            return engagementScore;
        }

        public void setEngagementScore(String engagementScore) {
            this.engagementScore = engagementScore;
        }

        public String getViralityScore() {
            return viralityScore;
        }

        public void setViralityScore(String viralityScore) {
            this.viralityScore = viralityScore;
        }
    }
}
