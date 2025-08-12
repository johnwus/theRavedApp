package com.raved.content.exception;

/**
 * ContentModerationException for TheRavedApp MongoDB
 */
public class ContentModerationException extends RuntimeException {

    private final String contentId;
    private final String contentType;
    private final String moderationAction;
    private final String errorCode;

    public ContentModerationException() {
        super();
        this.contentId = null;
        this.contentType = null;
        this.moderationAction = null;
        this.errorCode = "CONTENT_MODERATION_ERROR";
    }

    public ContentModerationException(String message) {
        super(message);
        this.contentId = null;
        this.contentType = null;
        this.moderationAction = null;
        this.errorCode = "CONTENT_MODERATION_ERROR";
    }

    public ContentModerationException(String message, Throwable cause) {
        super(message, cause);
        this.contentId = null;
        this.contentType = null;
        this.moderationAction = null;
        this.errorCode = "CONTENT_MODERATION_ERROR";
    }

    public ContentModerationException(String message, String contentId, String contentType, String moderationAction) {
        super(message);
        this.contentId = contentId;
        this.contentType = contentType;
        this.moderationAction = moderationAction;
        this.errorCode = "CONTENT_MODERATION_ERROR";
    }

    public ContentModerationException(String message, String contentId, String contentType, String moderationAction,
            String errorCode) {
        super(message);
        this.contentId = contentId;
        this.contentType = contentType;
        this.moderationAction = moderationAction;
        this.errorCode = errorCode;
    }

    public ContentModerationException(String message, String contentId, String contentType, String moderationAction,
            String errorCode, Throwable cause) {
        super(message, cause);
        this.contentId = contentId;
        this.contentType = contentType;
        this.moderationAction = moderationAction;
        this.errorCode = errorCode;
    }

    public String getContentId() {
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getModerationAction() {
        return moderationAction;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
