package com.raved.user.dto.request.settings;

public class PrivacySettingsUpdateRequest {
    private Boolean showActivity;
    private Boolean readReceipts;
    private Boolean allowDownloads;
    private Boolean allowStorySharing;

    public Boolean getShowActivity() { return showActivity; }
    public void setShowActivity(Boolean showActivity) { this.showActivity = showActivity; }

    public Boolean getReadReceipts() { return readReceipts; }
    public void setReadReceipts(Boolean readReceipts) { this.readReceipts = readReceipts; }

    public Boolean getAllowDownloads() { return allowDownloads; }
    public void setAllowDownloads(Boolean allowDownloads) { this.allowDownloads = allowDownloads; }

    public Boolean getAllowStorySharing() { return allowStorySharing; }
    public void setAllowStorySharing(Boolean allowStorySharing) { this.allowStorySharing = allowStorySharing; }
}

