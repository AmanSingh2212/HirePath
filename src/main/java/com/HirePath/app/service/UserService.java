package com.HirePath.app.service;

import com.HirePath.app.entity.User;

import java.time.LocalDateTime;

public interface UserService {

    String generateForgotPasswordOtp() throws Exception;

    boolean isForgotPasswordOtpValid(String inputOtp, String storedOtp, LocalDateTime expiryTime) throws Exception;

    User findByJwtToken(String jwt) throws Exception;



}
