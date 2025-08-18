package com.raved.content.controller;

import com.raved.content.dto.response.PostTagResponse;
import com.raved.content.model.PostTag;
import com.raved.content.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for tag operations MongoDB
 */
@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * Create a new tag
     */
    @PostMapping
    public ResponseEntity<PostTagResponse> createTag(@RequestBody PostTag tag) {
        var createdTag = tagService.createTag(tag);
        var response = convertToResponse(createdTag);
        return ResponseEntity.ok(response);
    }

    /**
     * Get tag by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostTagResponse> getTag(@PathVariable String id) {
        var tag = tagService.getTagById(id);
        if (tag.isPresent()) {
            var response = convertToResponse(tag.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get tag by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<PostTagResponse> getTagByName(@PathVariable String name) {
        var tag = tagService.getTagByName(name);
        if (tag.isPresent()) {
            var response = convertToResponse(tag.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update tag
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostTagResponse> updateTag(@PathVariable String id, @RequestBody PostTag tag) {
        var updatedTag = tagService.updateTag(id, tag);
        var response = convertToResponse(updatedTag);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete tag
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all tags with pagination
     */
    @GetMapping
    public ResponseEntity<Page<PostTagResponse>> getAllTags(Pageable pageable) {
        var tags = tagService.getAllTags(pageable);
        var responses = tags.map(this::convertToResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get tags by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PostTagResponse>> getTagsByCategory(@PathVariable String category) {
        var tags = tagService.getTagsByCategory(category);
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get trending tags
     */
    @GetMapping("/trending")
    public ResponseEntity<List<PostTagResponse>> getTrendingTags(@RequestParam(defaultValue = "10") int limit) {
        var tags = tagService.getTrendingTags(limit);
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get popular tags
     */
    @GetMapping("/popular")
    public ResponseEntity<List<PostTagResponse>> getPopularTags(@RequestParam(defaultValue = "10") int limit) {
        var tags = tagService.getPopularTags(limit);
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Search tags by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostTagResponse>> searchTags(@RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        var tags = tagService.searchTags(query, limit);
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get related tags
     */
    @GetMapping("/{name}/related")
    public ResponseEntity<List<PostTagResponse>> getRelatedTags(@PathVariable String name,
            @RequestParam(defaultValue = "10") int limit) {
        var tags = tagService.getRelatedTags(name, limit);
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Increment tag usage count
     */
    @PostMapping("/{name}/usage")
    public ResponseEntity<Void> incrementTagUsage(@PathVariable String name) {
        tagService.incrementTagUsage(name);
        return ResponseEntity.ok().build();
    }

    /**
     * Increment tag view count
     */
    @PostMapping("/{name}/view")
    public ResponseEntity<Void> incrementTagView(@PathVariable String name) {
        tagService.incrementTagView(name);
        return ResponseEntity.ok().build();
    }

    /**
     * Increment tag click count
     */
    @PostMapping("/{name}/click")
    public ResponseEntity<Void> incrementTagClick(@PathVariable String name) {
        tagService.incrementTagClick(name);
        return ResponseEntity.ok().build();
    }

    /**
     * Increment tag search count
     */
    @PostMapping("/{name}/search")
    public ResponseEntity<Void> incrementTagSearch(@PathVariable String name) {
        tagService.incrementTagSearch(name);
        return ResponseEntity.ok().build();
    }

    /**
     * Moderate tag
     */
    @PostMapping("/{id}/moderate")
    public ResponseEntity<Void> moderateTag(@PathVariable String id,
            @RequestParam String status,
            @RequestParam String reason,
            @RequestParam String moderatorId) {
        tagService.moderateTag(id, status, reason, moderatorId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get tags needing moderation
     */
    @GetMapping("/moderation/pending")
    public ResponseEntity<List<PostTagResponse>> getTagsNeedingModeration() {
        var tags = tagService.getTagsNeedingModeration();
        var responses = tags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get tag statistics
     */
    @GetMapping("/{id}/statistics")
    public ResponseEntity<TagService.TagStatistics> getTagStatistics(@PathVariable String id) {
        var stats = tagService.getTagStatistics(id);
        return ResponseEntity.ok(stats);
    }

    /**
     * Merge tags
     */
    @PostMapping("/merge")
    public ResponseEntity<PostTagResponse> mergeTags(@RequestParam String primaryTagId,
            @RequestBody List<String> tagIdsToMerge) {
        var mergedTag = tagService.mergeTags(primaryTagId, tagIdsToMerge);
        var response = convertToResponse(mergedTag);
        return ResponseEntity.ok(response);
    }

    /**
     * Split tag
     */
    @PostMapping("/{id}/split")
    public ResponseEntity<List<PostTagResponse>> splitTag(@PathVariable String id,
            @RequestBody List<String> newTagNames) {
        var newTags = tagService.splitTag(id, newTagNames);
        var responses = newTags.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Helper method to convert PostTag to PostTagResponse
     */
    private PostTagResponse convertToResponse(PostTag tag) {
        var response = new PostTagResponse();
        response.setId(tag.getId());
        response.setTagName(tag.getTagName());
        response.setDescription(tag.getDescription());
        response.setColor(tag.getColor());
        response.setIcon(tag.getIcon());
        response.setCategory(tag.getCategory());
        response.setUsageCount(tag.getUsageCount());
        response.setStatus(tag.getStatus());
        response.setCreatedAt(tag.getCreatedAt());
        response.setUpdatedAt(tag.getUpdatedAt());
        response.setLanguage(tag.getLanguage());
        response.setRegion(tag.getRegion());
        response.setCreatedBy(tag.getCreatedBy());
        response.setModeratedBy(tag.getModeratedBy());
        response.setModeratedAt(tag.getModeratedAt());
        response.setModerationReason(tag.getModerationReason());
        response.setViewCount(tag.getViewCount());
        response.setClickCount(tag.getClickCount());
        response.setSearchCount(tag.getSearchCount());
        response.setRelatedTags(tag.getRelatedTags());
        response.setParentTags(tag.getParentTags());
        response.setChildTags(tag.getChildTags());
        response.setMetadata(tag.getMetadata());
        return response;
    }
}
