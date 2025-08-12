package com.raved.content.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * MediaUploadRequest for TheRavedApp MongoDB
 */
@Data
public class MediaUploadRequest {

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotNull(message = "File type is required")
    private String fileType; // IMAGE, VIDEO, AUDIO, DOCUMENT

    // Alias method for compatibility with MediaServiceImpl
    public String getMediaType() {
        return fileType;
    }

    // Getter for isPublic field
    public Boolean isPublic() {
        return this.isPublic;
    }

    // Additional getter methods for compatibility
    public String getUploaderId() {
        return uploaderId;
    }

    public String getPostId() {
        return postId;
    }

    public String getLanguage() {
        return language;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    @NotNull(message = "MIME type is required")
    private String mimeType;

    @NotNull(message = "File size is required")
    private Long fileSize;

    private String postId; // Optional: if uploading for a specific post

    private String uploaderId;

    private String description;

    private String altText;

    private String language = "en";

    private String accessLevel = "PUBLIC";

    private Boolean isPublic = true;

    private List<String> allowedUserIds;

    private Map<String, Object> metadata;

    private String scheduledUploadAt; // ISO date string for scheduled uploads

    private Boolean generateThumbnail = true;

    private Boolean processMedia = true; // For videos/audio processing

    private Map<String, Object> processingOptions;
}
