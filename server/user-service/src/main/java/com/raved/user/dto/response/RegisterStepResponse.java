package com.raved.user.dto.response;

import java.util.List;

public class RegisterStepResponse {
    private boolean success;
    private Integer nextStep;
    private String sessionToken;
    private List<String> validationErrors;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public Integer getNextStep() { return nextStep; }
    public void setNextStep(Integer nextStep) { this.nextStep = nextStep; }
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public List<String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(List<String> validationErrors) { this.validationErrors = validationErrors; }
}

