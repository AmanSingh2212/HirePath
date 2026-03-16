package com.HirePath.app.service;

public interface EmailService {

    public void sendOtpEmail(String to, String otp) throws Exception;

}
