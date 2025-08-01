package com.raved.common.enums;

/**
 * Content type enumeration for posts and media
 */
public enum ContentType {
    TEXT("text", "Text content"),
    IMAGE("image", "Image content"),
    VIDEO("video", "Video content"),
    AUDIO("audio", "Audio content"),
    DOCUMENT("document", "Document content"),
    LINK("link", "Link/URL content"),
    POLL("poll", "Poll content"),
    EVENT("event", "Event content"),
    PRODUCT("product", "Product content"),
    STORY("story", "Story content");

    private final String code;
    private final String description;

    ContentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
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
}
