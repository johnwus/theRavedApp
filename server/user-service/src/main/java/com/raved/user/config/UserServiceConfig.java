package com.raved.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for User Service
 */
@Configuration
@ConfigurationProperties(prefix = "user-service")
public class UserServiceConfig {

    private FileUpload fileUpload = new FileUpload();
    private Email email = new Email();
    private RateLimit rateLimit = new RateLimit();
    private University university = new University();

    // Getters and Setters
    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    /**
     * File upload configuration
     */
    public static class FileUpload {
        private String maxSize = "10MB";
        private String[] allowedTypes = {"jpg", "jpeg", "png", "gif", "webp"};
        private String storagePath = "/uploads/users";

        // Getters and Setters
        public String getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(String maxSize) {
            this.maxSize = maxSize;
        }

        public String[] getAllowedTypes() {
            return allowedTypes;
        }

        public void setAllowedTypes(String[] allowedTypes) {
            this.allowedTypes = allowedTypes;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }
    }

    /**
     * Email configuration
     */
    public static class Email {
        private Verification verification = new Verification();
        private Templates templates = new Templates();

        public Verification getVerification() {
            return verification;
        }

        public void setVerification(Verification verification) {
            this.verification = verification;
        }

        public Templates getTemplates() {
            return templates;
        }

        public void setTemplates(Templates templates) {
            this.templates = templates;
        }

        public static class Verification {
            private boolean enabled = true;
            private int expiration = 24; // hours

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public int getExpiration() {
                return expiration;
            }

            public void setExpiration(int expiration) {
                this.expiration = expiration;
            }
        }

        public static class Templates {
            private String welcome = "welcome-template";
            private String verification = "verification-template";
            private String passwordReset = "password-reset-template";

            public String getWelcome() {
                return welcome;
            }

            public void setWelcome(String welcome) {
                this.welcome = welcome;
            }

            public String getVerification() {
                return verification;
            }

            public void setVerification(String verification) {
                this.verification = verification;
            }

            public String getPasswordReset() {
                return passwordReset;
            }

            public void setPasswordReset(String passwordReset) {
                this.passwordReset = passwordReset;
            }
        }
    }

    /**
     * Rate limiting configuration
     */
    public static class RateLimit {
        private Auth auth = new Auth();
        private Api api = new Api();

        public Auth getAuth() {
            return auth;
        }

        public void setAuth(Auth auth) {
            this.auth = auth;
        }

        public Api getApi() {
            return api;
        }

        public void setApi(Api api) {
            this.api = api;
        }

        public static class Auth {
            private int requests = 10;
            private int window = 60; // seconds

            public int getRequests() {
                return requests;
            }

            public void setRequests(int requests) {
                this.requests = requests;
            }

            public int getWindow() {
                return window;
            }

            public void setWindow(int window) {
                this.window = window;
            }
        }

        public static class Api {
            private int requests = 100;
            private int window = 60; // seconds

            public int getRequests() {
                return requests;
            }

            public void setRequests(int requests) {
                this.requests = requests;
            }

            public int getWindow() {
                return window;
            }

            public void setWindow(int window) {
                this.window = window;
            }
        }
    }

    /**
     * University verification configuration
     */
    public static class University {
        private Verification verification = new Verification();

        public Verification getVerification() {
            return verification;
        }

        public void setVerification(Verification verification) {
            this.verification = verification;
        }

        public static class Verification {
            private boolean autoApprove = false;
            private boolean adminReview = true;
            private boolean documentRequired = true;

            public boolean isAutoApprove() {
                return autoApprove;
            }

            public void setAutoApprove(boolean autoApprove) {
                this.autoApprove = autoApprove;
            }

            public boolean isAdminReview() {
                return adminReview;
            }

            public void setAdminReview(boolean adminReview) {
                this.adminReview = adminReview;
            }

            public boolean isDocumentRequired() {
                return documentRequired;
            }

            public void setDocumentRequired(boolean documentRequired) {
                this.documentRequired = documentRequired;
            }
        }
    }
}
