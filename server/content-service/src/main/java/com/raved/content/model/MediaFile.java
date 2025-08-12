package com.raved.content.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * MediaFile Document for TheRavedApp MongoDB
 *
 * Represents media files attached to posts.
 * Converted from JPA entity to MongoDB document.
 */
@Document(collection = "media_files")
public class MediaFile {

    @Id
    private String id;

    @Indexed
    private String postId; // Reference to post instead of embedded object

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    @TextIndexed(weight = 3)
    private String fileName;

    @Indexed
    private MediaType mediaType;

    @Indexed
    private Long fileSize; // in bytes

    @Size(max = 10, message = "File extension must not exceed 10 characters")
    private String fileExtension;

    @Indexed
    private Integer displayOrder = 0;

    // Image/Video specific fields
    private Integer width;

    private Integer height;

    private Integer durationSeconds; // for videos

    private String thumbnailUrl;

    @Size(max = 500, message = "Alt text must not exceed 500 characters")
    private String altText;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    // Additional MongoDB-specific fields for better media management
    private String originalFileName;

    @Indexed
    private String mimeType;

    @Indexed
    private String status = "UPLOADING"; // UPLOADING, PROCESSING, READY, FAILED

    private String storagePath;

    private String uploaderId;

    @Indexed
    private LocalDateTime uploadedAt;

    private LocalDateTime processedAt;

    // Processing metadata
    private String processingStatus;
    private String errorMessage;
    private Map<String, Object> processingMetadata;

    // Media-specific metadata
    private Map<String, Object> metadata;

    // Video-specific fields
    private String resolution;
    private String codec;
    private String format;

    // Document-specific fields
    private Integer pageCount;
    private String language;

    // Content analysis
    private String contentHash;
    private String exifData;

    // Access control
    @Indexed
    private String accessLevel = "PUBLIC";

    private Boolean isPublic = true;
    private List<String> allowedUserIds;

    // Analytics
    @Indexed
    private Integer viewCount = 0;

    @Indexed
    private Integer downloadCount = 0;

    @Indexed
    private Integer shareCount = 0;

    // Enums
    public enum MediaType {
        IMAGE, VIDEO, AUDIO, DOCUMENT
    }

    // Constructors
    public MediaFile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MediaFile(String postId, String fileUrl, String fileName, MediaType mediaType) {
        this();
        this.postId = postId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.mediaType = mediaType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // New MongoDB-specific getters and setters
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getProcessingMetadata() {
        return processingMetadata;
    }

    public void setProcessingMetadata(Map<String, Object> processingMetadata) {
        this.processingMetadata = processingMetadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getExifData() {
        return exifData;
    }

    public void setExifData(String exifData) {
        this.exifData = exifData;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<String> getAllowedUserIds() {
        return allowedUserIds;
    }

    public void setAllowedUserIds(List<String> allowedUserIds) {
        this.allowedUserIds = allowedUserIds;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    // Business logic methods
    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementDownloadCount() {
        if (this.downloadCount == null) {
            this.downloadCount = 0;
        }
        this.downloadCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementShareCount() {
        if (this.shareCount == null) {
            this.shareCount = 0;
        }
        this.shareCount++;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", mediaType=" + mediaType +
                ", fileUrl='" + fileUrl + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
