package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.constants.Pages;
import com.gmail.merikbest2015.ecommerce.constants.PathConstants;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.service.AuthenticationService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.AUTH)
public class AuthenticationController {

    private final AuthenticationService authService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/forgot")
    public String forgotPassword() {
        return Pages.FORGOT_PASSWORD;
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String phoneNumber, HttpServletRequest request, Model model) {
        MessageResponse response = authService.sendPasswordResetOtp(phoneNumber, request);
        return controllerUtils.setAlertMessage(model, Pages.FORGOT_PASSWORD, response);
    }

    @GetMapping("/reset")
    public String resetPasswordForm(@RequestParam String phone, Model model) {
        model.addAttribute("phone", phone);
        return Pages.RESET_PASSWORD;
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String phone,
                                @RequestParam String otpCode,
                                @RequestParam String newPassword,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request,
                                Model model) {
        MessageResponse messageResponse = authService.verifyPasswordResetOtp(phone, otpCode, newPassword, request);

        // Kiểm tra nếu message chứa thông báo lỗi
        if (messageResponse.getMessage().contains("Mã OTP không chính xác")) {
            model.addAttribute("phone", phone);
            model.addAttribute("otpError", messageResponse.getMessage());
            return Pages.RESET_PASSWORD;
        }

        return controllerUtils.setAlertFlashMessage(redirectAttributes, PathConstants.LOGIN, messageResponse);
    }
}
