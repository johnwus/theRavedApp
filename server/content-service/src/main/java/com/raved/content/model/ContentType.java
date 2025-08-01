package com.raved.content.model;

/**
 * Content type enumeration for posts and media
 */
public enum ContentType {
    TEXT("text", "Text content", true, false),
    IMAGE("image", "Image content", true, true),
    VIDEO("video", "Video content", true, true),
    AUDIO("audio", "Audio content", true, true),
    DOCUMENT("document", "Document content", true, true),
    LINK("link", "Link/URL content", true, false),
    POLL("poll", "Poll content", true, false),
    EVENT("event", "Event content", true, false),
    PRODUCT("product", "Product content", true, true),
    STORY("story", "Story content", true, true);

    private final String code;
    private final String description;
    private final boolean allowComments;
    private final boolean requiresMedia;

    ContentType(String code, String description, boolean allowComments, boolean requiresMedia) {
        this.code = code;
        this.description = description;
        this.allowComments = allowComments;
        this.requiresMedia = requiresMedia;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public boolean isRequiresMedia() {
        return requiresMedia;
    }

    public static ContentType fromCode(String code) {
        for (ContentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type code: " + code);
    }

    public boolean isMediaType() {
        return this == IMAGE || this == VIDEO || this == AUDIO;
    }

    public boolean isTextType() {
        return this == TEXT || this == LINK;
    }

    public boolean isInteractiveType() {
        return this == POLL || this == EVENT;
    }

    public boolean isTemporaryType() {
        return this == STORY;
    }

    public boolean isCommerceType() {
        return this == PRODUCT;
    }

    public int getMaxContentLength() {
        switch (this) {
            case TEXT:
                return 5000;
            case LINK:
                return 2000;
            case POLL:
                return 1000;
            case EVENT:
                return 3000;
            case PRODUCT:
                return 2000;
            case STORY:
                return 500;
            default:
                return 1000;
        }
    }

    public int getMaxMediaCount() {
        switch (this) {
            case IMAGE:
                return 10;
            case VIDEO:
                return 1;
            case AUDIO:
                return 1;
            case DOCUMENT:
                return 5;
            case PRODUCT:
                return 10;
            case STORY:
                return 1;
            default:
                return 0;
        }
    }

    public String[] getAllowedMediaTypes() {
        switch (this) {
            case IMAGE:
            case PRODUCT:
            case STORY:
                return new String[] { "jpg", "jpeg", "png", "gif", "webp" };
            case VIDEO:
                return new String[] { "mp4", "avi", "mov", "wmv" };
            case AUDIO:
                return new String[] { "mp3", "wav", "ogg", "m4a" };
            case DOCUMENT:
                return new String[] { "pdf", "doc", "docx", "txt" };
            default:
                return new String[] {};
        }
    }
}
