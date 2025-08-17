package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "page_views")
@CompoundIndex(name = "idx_user_path_time", def = "{'userId':1,'path':1,'viewedAt':-1}")
public class PageView {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String sessionId;

    @Indexed
    private String path;

    @Indexed
    private LocalDateTime viewedAt;

    private String deviceType;

    private String platform;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
}

