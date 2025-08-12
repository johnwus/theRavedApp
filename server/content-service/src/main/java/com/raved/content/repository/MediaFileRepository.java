package com.raved.content.repository;

import com.raved.content.model.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * MediaFileRepository for TheRavedApp MongoDB
 */
@Repository
public interface MediaFileRepository extends MongoRepository<MediaFile, String> {

    /**
     * Find media files by post ID
     */
    List<MediaFile> findByPostId(String postId);

    /**
     * Find media files by type
     */
    List<MediaFile> findByMediaType(MediaFile.MediaType mediaType);

    /**
     * Find media files by status
     */
    List<MediaFile> findByStatus(String status);

    /**
     * Find media files by uploader
     */
    List<MediaFile> findByUploaderId(String uploaderId);

    /**
     * Find media files uploaded after date
     */
    List<MediaFile> findByUploadedAtAfter(LocalDateTime date);

    /**
     * Find media files by MIME type
     */
    List<MediaFile> findByMimeType(String mimeType);

    /**
     * Find large media files
     */
    List<MediaFile> findByFileSizeGreaterThan(Long size);

    /**
     * Find media files by processing status
     */
    List<MediaFile> findByProcessingStatus(String processingStatus);

    /**
     * Find media files by partial filename
     */
    @Query("{'fileName': {$regex: ?0, $options: 'i'}}")
    List<MediaFile> findByFileNameContainingIgnoreCase(String fileName);

    /**
     * Find media files that need processing
     */
    @Query("{'status': 'UPLOADING', 'processingStatus': {$ne: 'COMPLETED'}}")
    List<MediaFile> findPendingProcessing();

    /**
     * Find media files by access level
     */
    List<MediaFile> findByAccessLevel(String accessLevel);

    /**
     * Find public media files
     */
    List<MediaFile> findByIsPublicTrue();

    /**
     * Find media files by dimensions
     */
    List<MediaFile> findByWidthAndHeight(Integer width, Integer height);

    /**
     * Find media files by duration range
     */
    @Query("{'durationSeconds': {$gte: ?0, $lte: ?1}}")
    List<MediaFile> findByDurationBetween(Integer minDuration, Integer maxDuration);

    /**
     * Find trending media files
     */
    @Query(value = "{}", sort = "{'viewCount': -1, 'downloadCount': -1}")
    List<MediaFile> findTrendingMedia(int limit);

    /**
     * Find recent media files
     */
    @Query(value = "{}", sort = "{'createdAt': -1}")
    List<MediaFile> findRecentMedia(int limit);

    /**
     * Find media files by content hash
     */
    MediaFile findByContentHash(String contentHash);

    /**
     * Count media files by user
     */
    long countByUploaderId(String uploaderId);

    /**
     * Count media files by type
     */
    long countByMediaType(MediaFile.MediaType mediaType);

    /**
     * Count media files by status
     */
    long countByStatus(String status);

    /**
     * Find media files by uploader with pagination
     */
    Page<MediaFile> findByUploaderId(String uploaderId, Pageable pageable);

    /**
     * Find media files by status ordered by view count descending
     */
    List<MediaFile> findByStatusOrderByViewCountDesc(String status);

    /**
     * Find media files by status ordered by upload date descending
     */
    List<MediaFile> findByStatusOrderByUploadedAtDesc(String status);

    /**
     * Find media files by file size range
     */
    List<MediaFile> findByFileSizeBetween(Long minSize, Long maxSize);

    /**
     * Find public media files ordered by upload date descending
     */
    List<MediaFile> findByIsPublicTrueOrderByUploadedAtDesc();

    /**
     * Find media files by language
     */
    List<MediaFile> findByLanguage(String language);
}
