package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.dto.request.UserRequest;

import javax.servlet.http.HttpServletRequest;

public interface RegistrationService {

    MessageResponse registration(UserRequest userRequest,HttpServletRequest request);

    MessageResponse sendOtpToZalo(String phone);

    MessageResponse verifyOtp(String phone, String otpCode, HttpServletRequest request);
}
