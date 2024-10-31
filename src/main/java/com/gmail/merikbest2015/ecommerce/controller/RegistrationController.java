package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.constants.Pages;
import com.gmail.merikbest2015.ecommerce.constants.PathConstants;
import com.gmail.merikbest2015.ecommerce.dto.request.UserRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.service.RegistrationService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String registration() {
        return Pages.REGISTRATION;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody UserRequest userRequest, HttpServletRequest request) {
        MessageResponse response = registrationService.registration(userRequest, request);
        return ResponseEntity.ok(response);
    }

    // Endpoint xác thực OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponse> verifyOtp(
            @RequestParam String phone,
            @RequestParam String otpCode,
            HttpServletRequest request) {
        MessageResponse response = registrationService.verifyOtp(phone, otpCode, request);
        return ResponseEntity.ok(response);
    }
}
