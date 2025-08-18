package com.raved.content.exception;

/**
 * MediaUploadException for TheRavedApp MongoDB
 */
public class MediaUploadException extends RuntimeException {

    private final String mediaId;
    private final String errorCode;

    public MediaUploadException() {
        super();
        this.mediaId = null;
        this.errorCode = "MEDIA_UPLOAD_ERROR";
    }

    public MediaUploadException(String message) {
        super(message);
        this.mediaId = null;
        this.errorCode = "MEDIA_UPLOAD_ERROR";
    }

    public MediaUploadException(String message, Throwable cause) {
        super(message, cause);
        this.mediaId = null;
        this.errorCode = "MEDIA_UPLOAD_ERROR";
    }

    public MediaUploadException(String message, String mediaId, String errorCode) {
        super(message);
        this.mediaId = mediaId;
        this.errorCode = errorCode;
    }

    public MediaUploadException(String message, String mediaId, String errorCode, Throwable cause) {
        super(message, cause);
        this.mediaId = mediaId;
        this.errorCode = errorCode;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
