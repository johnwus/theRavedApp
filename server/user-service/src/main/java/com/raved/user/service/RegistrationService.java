package com.raved.user.service;

import com.raved.user.dto.request.RegisterStepRequest;
import com.raved.user.dto.response.RegisterStepResponse;

public interface RegistrationService {
    RegisterStepResponse handleStep(RegisterStepRequest request);
}

