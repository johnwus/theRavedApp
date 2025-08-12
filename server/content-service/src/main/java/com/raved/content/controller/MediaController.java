package com.raved.content.controller;

import com.raved.content.dto.request.MediaUploadRequest;
import com.raved.content.dto.response.MediaResponse;
import com.raved.content.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for media operations MongoDB
 */
@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    /**
     * Upload media file
     */
    @PostMapping("/upload")
    public ResponseEntity<MediaResponse> uploadMedia(@RequestParam("file") MultipartFile file,
            @ModelAttribute MediaUploadRequest request) {
        var response = mediaService.uploadMedia(file, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get media by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getMedia(@PathVariable String id) {
        var media = mediaService.getMediaById(id);
        if (media.isPresent()) {
            return ResponseEntity.ok(media.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update media metadata
     */
    @PutMapping("/{id}")
    public ResponseEntity<MediaResponse> updateMedia(@PathVariable String id,
            @RequestBody MediaUploadRequest request) {
        var response = mediaService.updateMedia(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete media
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get media by post ID
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<MediaResponse>> getMediaByPost(@PathVariable String postId) {
        var media = mediaService.getMediaByPostId(postId);
        return ResponseEntity.ok(media);
    }

    /**
     * Get media by uploader
     */
    @GetMapping("/uploader/{uploaderId}")
    public ResponseEntity<Page<MediaResponse>> getMediaByUploader(@PathVariable String uploaderId,
            Pageable pageable) {
        var media = mediaService.getMediaByUploader(uploaderId, pageable);
        return ResponseEntity.ok(media);
    }

    /**
     * Get trending media
     */
    @GetMapping("/trending")
    public ResponseEntity<List<MediaResponse>> getTrendingMedia(@RequestParam(defaultValue = "10") int limit) {
        var media = mediaService.getTrendingMedia(limit);
        return ResponseEntity.ok(media);
    }

    /**
     * Get recent media
     */
    @GetMapping("/recent")
    public ResponseEntity<List<MediaResponse>> getRecentMedia(@RequestParam(defaultValue = "10") int limit) {
        var media = mediaService.getRecentMedia(limit);
        return ResponseEntity.ok(media);
    }

    /**
     * Search media
     */
    @GetMapping("/search")
    public ResponseEntity<List<MediaResponse>> searchMedia(@RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        var media = mediaService.searchMedia(query, limit);
        return ResponseEntity.ok(media);
    }

    /**
     * Process media file
     */
    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processMedia(@PathVariable String id) {
        mediaService.processMedia(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Generate thumbnail
     */
    @PostMapping("/{id}/thumbnail")
    public ResponseEntity<String> generateThumbnail(@PathVariable String id) {
        var thumbnailUrl = mediaService.generateThumbnail(id);
        return ResponseEntity.ok(thumbnailUrl);
    }

    /**
     * Increment view count
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable String id) {
        mediaService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Increment download count
     */
    @PostMapping("/{id}/download")
    public ResponseEntity<Void> incrementDownloadCount(@PathVariable String id) {
        mediaService.incrementDownloadCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Increment share count
     */
    @PostMapping("/{id}/share")
    public ResponseEntity<Void> incrementShareCount(@PathVariable String id) {
        mediaService.incrementShareCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get media statistics
     */
    @GetMapping("/{id}/statistics")
    public ResponseEntity<MediaService.MediaStatistics> getMediaStatistics(@PathVariable String id) {
        var stats = mediaService.getMediaStatistics(id);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get media analytics
     */
    @GetMapping("/{id}/analytics")
    public ResponseEntity<MediaService.MediaAnalytics> getMediaAnalytics(@PathVariable String id) {
        var analytics = mediaService.getMediaAnalytics(id);
        return ResponseEntity.ok(analytics);
    }

    /**
     * Retry failed media processing
     */
    @PostMapping("/{id}/retry")
    public ResponseEntity<Void> retryMediaProcessing(@PathVariable String id) {
        mediaService.retryMediaProcessing(id);
        return ResponseEntity.ok().build();
    }
}
