package com.raved.user.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class RegisterStepRequest {
    @NotNull
    private Integer step;
    private String sessionToken;
    private Map<String, Object> data;

    public Integer getStep() { return step; }
    public void setStep(Integer step) { this.step = step; }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}

