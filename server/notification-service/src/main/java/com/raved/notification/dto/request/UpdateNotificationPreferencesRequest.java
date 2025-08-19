package com.raved.notification.dto.request;

import jakarta.validation.constraints.NotNull;

public class UpdateNotificationPreferencesRequest {
    @NotNull
    private Channels channels = new Channels();

    private QuietHours quietHours = new QuietHours();

    public Channels getChannels() { return channels; }
    public void setChannels(Channels channels) { this.channels = channels; }
    public QuietHours getQuietHours() { return quietHours; }
    public void setQuietHours(QuietHours quietHours) { this.quietHours = quietHours; }

    public static class Channels {
        private boolean pushEnabled = true;
        private boolean emailEnabled = true;
        private boolean smsEnabled = false;

        public boolean isPushEnabled() { return pushEnabled; }
        public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }
        public boolean isEmailEnabled() { return emailEnabled; }
        public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }
        public boolean isSmsEnabled() { return smsEnabled; }
        public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }
    }

    public static class QuietHours {
        private boolean enabled = false;
        private String startTime = "22:00";
        private String endTime = "08:00";
        private String timezone = "UTC";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
    }
}

