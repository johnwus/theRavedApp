package com.raved.notification.dto.response;

public class NotificationPreferencesResponse {
    private boolean pushEnabled;
    private boolean emailEnabled;
    private boolean smsEnabled;

    private boolean quietHours;
    private String quietStart;
    private String quietEnd;
    private String timezone;

    public boolean isPushEnabled() { return pushEnabled; }
    public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }
    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }
    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }

    public boolean isQuietHours() { return quietHours; }
    public void setQuietHours(boolean quietHours) { this.quietHours = quietHours; }
    public String getQuietStart() { return quietStart; }
    public void setQuietStart(String quietStart) { this.quietStart = quietStart; }
    public String getQuietEnd() { return quietEnd; }
    public void setQuietEnd(String quietEnd) { this.quietEnd = quietEnd; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}

