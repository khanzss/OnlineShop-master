package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.constants.SuccessMessage;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.repository.UserRepository;
import com.gmail.merikbest2015.ecommerce.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;

    @Value("${zalo.otp.url}")
    private String zaloOtpUrl;

    @Value("${zalo.otp.secret}")
    private String zaloSecret;

    private static final int OTP_LIMIT = 3; // Số lần yêu cầu tối đa
    private static final long BLOCK_DURATION = 5; // Khoảng thời gian khóa (5 phút)
    private static final SecureRandom random = new SecureRandom();

    // Gửi mã OTP đặt lại mật khẩu qua số điện thoại
    @Override
    @Transactional
    public MessageResponse sendPasswordResetOtp(String phoneNumber, HttpServletRequest request) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return new MessageResponse("alert-danger", ErrorMessage.PHONE_NUMBER_NOT_FOUND);
        }

        // Kiểm tra giới hạn OTP dựa trên IP và thiết bị
        String clientKey = getClientKey(request);
        if (isBlocked(clientKey)) {
            return new MessageResponse("alert-danger", "Vui lòng thử lại sau 5 phút.");
        }

        // Gửi OTP qua Zalo
        return sendOtpToZalo(phoneNumber, clientKey);
    }

    // Xác thực mã OTP và đặt lại mật khẩu
    @Override
    @Transactional
    public MessageResponse verifyPasswordResetOtp(String phone, String otpCode, String newPassword, HttpServletRequest request) {
        // Kiểm tra mã OTP đã lưu trong Redis
        String clientKey = getClientKey(request);
        String savedOtp = redisTemplate.opsForValue().get(clientKey + ":otp");

        if (savedOtp != null && savedOtp.equals(otpCode)) {
            // Xác thực thành công
            User user = userRepository.findByPhoneNumber(phone);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                redisTemplate.delete(clientKey + ":otp"); // Xóa OTP sau khi xác thực
                return new MessageResponse("alert-success", "Mật khẩu đã được đặt lại thành công!");
            }
        }
        return new MessageResponse("alert-danger", "Mã OTP không chính xác.");
    }

    // Tạo clientKey dựa trên địa chỉ IP và User-Agent
    private String getClientKey(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        return ipAddress + ":" + userAgent;
    }

    // Kiểm tra nếu clientKey đã đạt giới hạn OTP
    private boolean isBlocked(String clientKey) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attemptCount = ops.get(clientKey);
        return attemptCount != null && Integer.parseInt(attemptCount) >= OTP_LIMIT;
    }

    // Gửi OTP qua Zalo
    private MessageResponse sendOtpToZalo(String phone, String clientKey) {
        // Tăng số lần yêu cầu OTP
        incrementOtpAttempts(clientKey);

        // Tạo mã OTP ngẫu nhiên 6 chữ số
        String otpCode = generateOtpCode();
        String otpUrl = String.format(zaloOtpUrl, zaloSecret, phone);

        // Gửi OTP qua API Zalo
        Map<String, String> otpRequest = new HashMap<>();
        otpRequest.put("otpCode", otpCode);
        otpRequest.put("phone", phone);
        restTemplate.postForObject(otpUrl, otpRequest, Void.class);

        // Lưu mã OTP vào Redis với thời hạn 5 phút
        saveOtpToRedis(clientKey, otpCode);

        return new MessageResponse("alert-success", "OTP đã được gửi qua Zalo.");
    }

    private String generateOtpCode() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void incrementOtpAttempts(String clientKey) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attemptCount = ops.get(clientKey);
        if (attemptCount == null) {
            ops.set(clientKey, "1", BLOCK_DURATION, TimeUnit.MINUTES);
        } else {
            int newCount = Integer.parseInt(attemptCount) + 1;
            ops.set(clientKey, String.valueOf(newCount), BLOCK_DURATION, TimeUnit.MINUTES);
        }
    }

    private void saveOtpToRedis(String clientKey, String otpCode) {
        redisTemplate.opsForValue().set(clientKey + ":otp", otpCode, 5, TimeUnit.MINUTES);
    }
}
