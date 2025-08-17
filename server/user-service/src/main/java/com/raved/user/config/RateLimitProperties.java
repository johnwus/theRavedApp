package com.raved.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {
    private boolean enabled = true;
    private List<Rule> rules = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static class Rule {
        private String path;
        private String method = "GET";
        private String key = "ip"; // ip | destination
        private int capacity = 10;
        private int refillPerMinute = 10;

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
        public int getRefillPerMinute() { return refillPerMinute; }
        public void setRefillPerMinute(int refillPerMinute) { this.refillPerMinute = refillPerMinute; }
    }
}

