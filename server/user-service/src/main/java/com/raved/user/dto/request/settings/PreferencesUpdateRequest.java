package com.raved.user.dto.request.settings;

public class PreferencesUpdateRequest {
    private Boolean analyticsEnabled;
    private Boolean personalizedAds;
    private String language;
    private String dateFormat;
    private String currency;
    private String theme;
    private String notificationPreferencesJson; // JSON string

    public Boolean getAnalyticsEnabled() { return analyticsEnabled; }
    public void setAnalyticsEnabled(Boolean analyticsEnabled) { this.analyticsEnabled = analyticsEnabled; }

    public Boolean getPersonalizedAds() { return personalizedAds; }
    public void setPersonalizedAds(Boolean personalizedAds) { this.personalizedAds = personalizedAds; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getNotificationPreferencesJson() { return notificationPreferencesJson; }
    public void setNotificationPreferencesJson(String notificationPreferencesJson) { this.notificationPreferencesJson = notificationPreferencesJson; }
}

