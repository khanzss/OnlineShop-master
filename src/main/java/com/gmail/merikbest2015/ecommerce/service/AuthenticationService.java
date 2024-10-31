package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.dto.request.PasswordResetRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    MessageResponse sendPasswordResetOtp(String phoneNumber, HttpServletRequest request);


    MessageResponse verifyPasswordResetOtp(String phone, String otpCode, String newPassword, HttpServletRequest request);
}
