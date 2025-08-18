package com.raved.content.service.impl;

import com.raved.content.dto.request.MediaUploadRequest;
import com.raved.content.dto.response.MediaResponse;
import com.raved.content.model.MediaFile;
import com.raved.content.repository.MediaFileRepository;
import com.raved.content.service.MediaService;
import com.raved.content.service.MediaService.MediaAnalytics;
import com.raved.content.service.MediaService.MediaStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MediaService implementation for TheRavedApp MongoDB
 */
@Service
public class MediaServiceImpl implements MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Override
    public MediaResponse uploadMedia(MultipartFile file, MediaUploadRequest request) {
        logger.info("Uploading media file: {}", file.getOriginalFilename());

        try {
            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(file.getOriginalFilename());
            mediaFile.setFileSize(file.getSize());
            mediaFile.setMimeType(file.getContentType());
            mediaFile.setUploaderId(request.getUploaderId());
            mediaFile.setPostId(request.getPostId());
            try {
                MediaFile.MediaType type = MediaFile.MediaType.valueOf(request.getMediaType().toUpperCase());
                mediaFile.setMediaType(type);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid media type: {}, using default", request.getMediaType());
                mediaFile.setMediaType(MediaFile.MediaType.IMAGE); // Default fallback
            }
            mediaFile.setStatus("UPLOADED");
            mediaFile.setProcessingStatus("PENDING");
            mediaFile.setAccessLevel(request.getAccessLevel());
            mediaFile.setIsPublic(request.isPublic());

            mediaFile.setLanguage(request.getLanguage());
            mediaFile.setUploadedAt(LocalDateTime.now());
            mediaFile.setUpdatedAt(LocalDateTime.now());

            MediaFile savedMedia = mediaFileRepository.save(mediaFile);
            logger.info("Media uploaded successfully with ID: {}", savedMedia.getId());

            return convertToMediaResponse(savedMedia);
        } catch (Exception e) {
            logger.error("Error uploading media file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload media file", e);
        }
    }

    @Override
    public Optional<MediaResponse> getMediaById(String id) {
        logger.debug("Getting media by ID: {}", id);
        return mediaFileRepository.findById(id)
                .map(this::convertToMediaResponse);
    }

    @Override
    public MediaResponse updateMedia(String id, MediaUploadRequest request) {
        logger.info("Updating media with ID: {}", id);

        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with ID: " + id));

        mediaFile.setLanguage(request.getLanguage());
        mediaFile.setAccessLevel(request.getAccessLevel());
        mediaFile.setIsPublic(request.isPublic());
        mediaFile.setUpdatedAt(LocalDateTime.now());

        MediaFile updatedMedia = mediaFileRepository.save(mediaFile);
        logger.info("Media updated successfully: {}", id);

        return convertToMediaResponse(updatedMedia);
    }

    @Override
    public void deleteMedia(String id) {
        logger.info("Deleting media with ID: {}", id);
        mediaFileRepository.deleteById(id);
        logger.info("Media deleted successfully: {}", id);
    }

    @Override
    public List<MediaResponse> getMediaByPostId(String postId) {
        logger.debug("Getting media by post ID: {}", postId);
        return mediaFileRepository.findByPostId(postId)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MediaResponse> getMediaByUploader(String uploaderId, Pageable pageable) {
        logger.debug("Getting media by uploader: {}", uploaderId);
        return mediaFileRepository.findByUploaderId(uploaderId, pageable)
                .map(this::convertToMediaResponse);
    }

    @Override
    public List<MediaResponse> getMediaByType(String mediaType) {
        logger.debug("Getting media by type: {}", mediaType);
        try {
            MediaFile.MediaType type = MediaFile.MediaType.valueOf(mediaType.toUpperCase());
            return mediaFileRepository.findByMediaType(type)
                    .stream()
                    .map(this::convertToMediaResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid media type: {}", mediaType);
            return new ArrayList<>();
        }
    }

    @Override
    public List<MediaResponse> getMediaByStatus(String status) {
        logger.debug("Getting media by status: {}", status);
        return mediaFileRepository.findByStatus(status)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getTrendingMedia(int limit) {
        logger.debug("Getting trending media with limit: {}", limit);
        return mediaFileRepository.findByStatusOrderByViewCountDesc("PROCESSED")
                .stream()
                .limit(limit)
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getRecentMedia(int limit) {
        logger.debug("Getting recent media with limit: {}", limit);
        return mediaFileRepository.findByStatusOrderByUploadedAtDesc("PROCESSED")
                .stream()
                .limit(limit)
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> searchMedia(String query, int limit) {
        logger.debug("Searching media with query: {}", query);
        // This would typically use a text search index
        return mediaFileRepository.findByFileNameContainingIgnoreCase(query)
                .stream()
                .limit(limit)
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void processMedia(String mediaId) {
        logger.info("Processing media: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setProcessingStatus("PROCESSING");
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);

        // In a real implementation, this would trigger async processing
        logger.info("Media processing started: {}", mediaId);
    }

    @Override
    public String generateThumbnail(String mediaId) {
        logger.info("Generating thumbnail for media: {}", mediaId);
        // In a real implementation, this would generate and store thumbnail
        return "thumbnail_" + mediaId + ".jpg";
    }

    @Override
    public void updateMediaStatus(String mediaId, String status) {
        logger.info("Updating media status: {} to {}", mediaId, status);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setStatus(status);
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public void updateProcessingStatus(String mediaId, String processingStatus) {
        logger.info("Updating processing status: {} to {}", mediaId, processingStatus);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setProcessingStatus(processingStatus);
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public MediaStatistics getMediaStatistics(String mediaId) {
        logger.debug("Getting media statistics for: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        MediaStatistics stats = new MediaStatistics();
        stats.setTotalViews(mediaFile.getViewCount() != null ? mediaFile.getViewCount() : 0);
        stats.setTotalDownloads(mediaFile.getDownloadCount() != null ? mediaFile.getDownloadCount() : 0);
        stats.setTotalShares(mediaFile.getShareCount() != null ? mediaFile.getShareCount() : 0);
        stats.setAverageRating(0.0); // Would calculate from ratings
        stats.setPopularityScore("MEDIUM"); // Would calculate from engagement

        return stats;
    }

    @Override
    public void incrementViewCount(String mediaId) {
        logger.debug("Incrementing view count for media: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setViewCount((mediaFile.getViewCount() != null ? mediaFile.getViewCount() : 0) + 1);
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public void incrementDownloadCount(String mediaId) {
        logger.debug("Incrementing download count for media: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setDownloadCount((mediaFile.getDownloadCount() != null ? mediaFile.getDownloadCount() : 0) + 1);
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public void incrementShareCount(String mediaId) {
        logger.debug("Incrementing share count for media: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setShareCount((mediaFile.getShareCount() != null ? mediaFile.getShareCount() : 0) + 1);
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public List<MediaResponse> getMediaByDimensions(Integer width, Integer height) {
        logger.debug("Getting media by dimensions: {}x{}", width, height);
        return mediaFileRepository.findByWidthAndHeight(width, height)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediaByDuration(Integer minDuration, Integer maxDuration) {
        logger.debug("Getting media by duration range: {} to {}", minDuration, maxDuration);
        return mediaFileRepository.findByDurationBetween(minDuration, maxDuration)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediaByFileSize(Long minSize, Long maxSize) {
        logger.debug("Getting media by file size range: {} to {}", minSize, maxSize);
        return mediaFileRepository.findByFileSizeBetween(minSize, maxSize)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediaByMimeType(String mimeType) {
        logger.debug("Getting media by MIME type: {}", mimeType);
        return mediaFileRepository.findByMimeType(mimeType)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediaByAccessLevel(String accessLevel) {
        logger.debug("Getting media by access level: {}", accessLevel);
        return mediaFileRepository.findByAccessLevel(accessLevel)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getPublicMedia(int limit) {
        logger.debug("Getting public media with limit: {}", limit);
        return mediaFileRepository.findByIsPublicTrueOrderByUploadedAtDesc()
                .stream()
                .limit(limit)
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediaByLanguage(String language) {
        logger.debug("Getting media by language: {}", language);
        return mediaFileRepository.findByLanguage(language)
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MediaResponse> getMediaByContentHash(String contentHash) {
        logger.debug("Getting media by content hash: {}", contentHash);
        MediaFile mediaFile = mediaFileRepository.findByContentHash(contentHash);
        if (mediaFile != null) {
            return Optional.of(convertToMediaResponse(mediaFile));
        }
        return Optional.empty();
    }

    @Override
    public List<MediaResponse> getPendingProcessingMedia() {
        logger.debug("Getting pending processing media");
        return mediaFileRepository.findByProcessingStatus("PENDING")
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getFailedMedia() {
        logger.debug("Getting failed media");
        return mediaFileRepository.findByProcessingStatus("FAILED")
                .stream()
                .map(this::convertToMediaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void retryMediaProcessing(String mediaId) {
        logger.info("Retrying media processing: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        mediaFile.setProcessingStatus("PENDING");
        mediaFile.setUpdatedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);

        logger.info("Media processing retry initiated: {}", mediaId);
    }

    @Override
    public MediaAnalytics getMediaAnalytics(String mediaId) {
        logger.debug("Getting media analytics for: {}", mediaId);
        MediaFile mediaFile = mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        MediaAnalytics analytics = new MediaAnalytics();
        analytics.setMediaId(mediaId);
        analytics.setViewTrend("INCREASING"); // Would calculate from historical data
        analytics.setDownloadTrend("STABLE"); // Would calculate from historical data
        analytics.setShareTrend("INCREASING"); // Would calculate from historical data
        analytics.setEngagementScore("HIGH"); // Would calculate from engagement metrics
        analytics.setViralityScore("MEDIUM"); // Would calculate from virality metrics

        return analytics;
    }

    /**
     * Convert MediaFile to MediaResponse
     */
    private MediaResponse convertToMediaResponse(MediaFile mediaFile) {
        MediaResponse response = new MediaResponse();
        response.setId(mediaFile.getId());
        response.setFileName(mediaFile.getFileName());
        response.setFileSize(mediaFile.getFileSize());
        response.setMimeType(mediaFile.getMimeType());
        response.setUploaderId(mediaFile.getUploaderId());
        response.setPostId(mediaFile.getPostId());
        response.setMediaType(mediaFile.getMediaType() != null ? mediaFile.getMediaType().name() : null);
        response.setStatus(mediaFile.getStatus());
        response.setProcessingStatus(mediaFile.getProcessingStatus());
        response.setAccessLevel(mediaFile.getAccessLevel());
        response.setIsPublic(mediaFile.getIsPublic());
        response.setLanguage(mediaFile.getLanguage());
        response.setUploadedAt(mediaFile.getUploadedAt());
        response.setUpdatedAt(mediaFile.getUpdatedAt());
        response.setViewCount(mediaFile.getViewCount());
        response.setDownloadCount(mediaFile.getDownloadCount());
        response.setShareCount(mediaFile.getShareCount());

        return response;
    }
}
