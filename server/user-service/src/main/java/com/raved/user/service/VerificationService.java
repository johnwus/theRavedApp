package com.raved.user.service;

public interface VerificationService {
    void sendEmailCode(String email, String purpose);
    void sendPhoneCode(String phone, String purpose);
    boolean verifyEmailCode(String email, String code, String purpose);
    boolean verifyPhoneCode(String phone, String code, String purpose);
}

