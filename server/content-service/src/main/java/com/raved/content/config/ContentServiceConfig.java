package com.raved.content.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Content Service
 */
@Configuration
@ConfigurationProperties(prefix = "content")
public class ContentServiceConfig {

    private Moderation moderation = new Moderation();
    private Media media = new Media();
    private Feed feed = new Feed();
    private ContentTypes contentTypes = new ContentTypes();
    private Analytics analytics = new Analytics();
    private Notifications notifications = new Notifications();

    // Getters and Setters
    public Moderation getModeration() {
        return moderation;
    }

    public void setModeration(Moderation moderation) {
        this.moderation = moderation;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public ContentTypes getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(ContentTypes contentTypes) {
        this.contentTypes = contentTypes;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Analytics analytics) {
        this.analytics = analytics;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    /**
     * Content moderation configuration
     */
    public static class Moderation {
        private boolean enabled = true;
        private boolean autoApprove = false;
        private boolean aiModeration = true;
        private boolean profanityFilter = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isAutoApprove() {
            return autoApprove;
        }

        public void setAutoApprove(boolean autoApprove) {
            this.autoApprove = autoApprove;
        }

        public boolean isAiModeration() {
            return aiModeration;
        }

        public void setAiModeration(boolean aiModeration) {
            this.aiModeration = aiModeration;
        }

        public boolean isProfanityFilter() {
            return profanityFilter;
        }

        public void setProfanityFilter(boolean profanityFilter) {
            this.profanityFilter = profanityFilter;
        }
    }

    /**
     * Media configuration
     */
    public static class Media {
        private String maxFileSize = "100MB";
        private String[] allowedImageTypes = {"jpg", "jpeg", "png", "gif", "webp"};
        private String[] allowedVideoTypes = {"mp4", "avi", "mov", "wmv"};
        private String storagePath = "/uploads/content";
        private String cdnUrl = "http://localhost:8080/media";

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String[] getAllowedImageTypes() {
            return allowedImageTypes;
        }

        public void setAllowedImageTypes(String[] allowedImageTypes) {
            this.allowedImageTypes = allowedImageTypes;
        }

        public String[] getAllowedVideoTypes() {
            return allowedVideoTypes;
        }

        public void setAllowedVideoTypes(String[] allowedVideoTypes) {
            this.allowedVideoTypes = allowedVideoTypes;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }

        public String getCdnUrl() {
            return cdnUrl;
        }

        public void setCdnUrl(String cdnUrl) {
            this.cdnUrl = cdnUrl;
        }
    }

    /**
     * Feed configuration
     */
    public static class Feed {
        private int pageSize = 20;
        private int cacheTtl = 300; // seconds
        private String trendingAlgorithm = "engagement-score";

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCacheTtl() {
            return cacheTtl;
        }

        public void setCacheTtl(int cacheTtl) {
            this.cacheTtl = cacheTtl;
        }

        public String getTrendingAlgorithm() {
            return trendingAlgorithm;
        }

        public void setTrendingAlgorithm(String trendingAlgorithm) {
            this.trendingAlgorithm = trendingAlgorithm;
        }
    }

    /**
     * Content types configuration
     */
    public static class ContentTypes {
        private Text text = new Text();
        private Image image = new Image();
        private Video video = new Video();
        private Poll poll = new Poll();

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Video getVideo() {
            return video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public Poll getPoll() {
            return poll;
        }

        public void setPoll(Poll poll) {
            this.poll = poll;
        }

        public static class Text {
            private int maxLength = 5000;
            private boolean allowHtml = false;

            public int getMaxLength() {
                return maxLength;
            }

            public void setMaxLength(int maxLength) {
                this.maxLength = maxLength;
            }

            public boolean isAllowHtml() {
                return allowHtml;
            }

            public void setAllowHtml(boolean allowHtml) {
                this.allowHtml = allowHtml;
            }
        }

        public static class Image {
            private int maxCount = 10;
            private boolean compression = true;
            private boolean thumbnailGeneration = true;

            public int getMaxCount() {
                return maxCount;
            }

            public void setMaxCount(int maxCount) {
                this.maxCount = maxCount;
            }

            public boolean isCompression() {
                return compression;
            }

            public void setCompression(boolean compression) {
                this.compression = compression;
            }

            public boolean isThumbnailGeneration() {
                return thumbnailGeneration;
            }

            public void setThumbnailGeneration(boolean thumbnailGeneration) {
                this.thumbnailGeneration = thumbnailGeneration;
            }
        }

        public static class Video {
            private int maxDuration = 600; // seconds
            private boolean transcoding = true;
            private boolean thumbnailGeneration = true;

            public int getMaxDuration() {
                return maxDuration;
            }

            public void setMaxDuration(int maxDuration) {
                this.maxDuration = maxDuration;
            }

            public boolean isTranscoding() {
                return transcoding;
            }

            public void setTranscoding(boolean transcoding) {
                this.transcoding = transcoding;
            }

            public boolean isThumbnailGeneration() {
                return thumbnailGeneration;
            }

            public void setThumbnailGeneration(boolean thumbnailGeneration) {
                this.thumbnailGeneration = thumbnailGeneration;
            }
        }

        public static class Poll {
            private int maxOptions = 10;
            private int maxDuration = 7; // days

            public int getMaxOptions() {
                return maxOptions;
            }

            public void setMaxOptions(int maxOptions) {
                this.maxOptions = maxOptions;
            }

            public int getMaxDuration() {
                return maxDuration;
            }

            public void setMaxDuration(int maxDuration) {
                this.maxDuration = maxDuration;
            }
        }
    }

    /**
     * Analytics integration configuration
     */
    public static class Analytics {
        private boolean enabled = true;
        private boolean trackViews = true;
        private boolean trackEngagement = true;
        private int batchSize = 100;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isTrackViews() {
            return trackViews;
        }

        public void setTrackViews(boolean trackViews) {
            this.trackViews = trackViews;
        }

        public boolean isTrackEngagement() {
            return trackEngagement;
        }

        public void setTrackEngagement(boolean trackEngagement) {
            this.trackEngagement = trackEngagement;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }

    /**
     * Notification integration configuration
     */
    public static class Notifications {
        private boolean enabled = true;
        private int likeThreshold = 10;
        private boolean commentNotifications = true;
        private boolean mentionNotifications = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getLikeThreshold() {
            return likeThreshold;
        }

        public void setLikeThreshold(int likeThreshold) {
            this.likeThreshold = likeThreshold;
        }

        public boolean isCommentNotifications() {
            return commentNotifications;
        }

        public void setCommentNotifications(boolean commentNotifications) {
            this.commentNotifications = commentNotifications;
        }

        public boolean isMentionNotifications() {
            return mentionNotifications;
        }

        public void setMentionNotifications(boolean mentionNotifications) {
            this.mentionNotifications = mentionNotifications;
        }
    }
}
