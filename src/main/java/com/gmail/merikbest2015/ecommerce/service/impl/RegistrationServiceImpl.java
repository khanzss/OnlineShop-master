package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.domain.Role;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.dto.request.UserRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.repository.UserRepository;
import com.gmail.merikbest2015.ecommerce.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final StringRedisTemplate redisTemplate;

    @Value("${zalo.otp.url}")
    private String zaloOtpUrl;

    @Value("${zalo.otp.secret}")
    private String zaloSecret;

    private static final int OTP_LIMIT = 3; // Số lần yêu cầu tối đa
    private static final long BLOCK_DURATION = 5; // Khoảng thời gian khóa (5 phút)
    private static final SecureRandom random = new SecureRandom();

    @Override
    @Transactional
    public MessageResponse registration(UserRequest userRequest, HttpServletRequest request) {
        if (!userRequest.getPassword().equals(userRequest.getPassword2())) {
            return new MessageResponse("passwordError", "Mật khẩu không khớp.");
        }

        if (userRepository.findByPhoneNumber(userRequest.getPhone()) != null) {
            return new MessageResponse("phoneError", "Số điện thoại đã được sử dụng.");
        }

        // Kiểm tra giới hạn OTP dựa trên IP và thiết bị
        String clientKey = getClientKey(request);
        if (isBlocked(clientKey)) {
            return new MessageResponse("alert-danger", "Vui lòng thử lại sau 5 phút.");
        }

        // Tạo người dùng mới và lưu vào CSDL
        User user = modelMapper.map(userRequest, User.class);
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setCreateDate(LocalDateTime.now());
        userRepository.save(user);

        // Gửi OTP qua Zalo
        return sendOtpToZalo(userRequest.getPhone(), clientKey);
    }

    // Tạo clientKey dựa trên địa chỉ IP và User-Agent
    private String getClientKey(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr(); // Lấy địa chỉ IP
        String userAgent = request.getHeader("User-Agent"); // Lấy User-Agent (thiết bị)
        return ipAddress + ":" + userAgent; // Kết hợp thành key duy nhất
    }

    private boolean isBlocked(String clientKey) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attemptCount = ops.get(clientKey);

        if (attemptCount != null && Integer.parseInt(attemptCount) >= OTP_LIMIT) {
            return true; // Đã vượt quá giới hạn yêu cầu OTP
        }
        return false;
    }

//    @Override
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
        int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 đến 999999
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
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(clientKey + ":otp", otpCode, 5, TimeUnit.MINUTES); // Lưu mã OTP với thời hạn 5 phút
    }

    @Override
    public MessageResponse verifyOtp(String phone, String otpCode, HttpServletRequest request) {
        // Lấy clientKey từ IP và User-Agent để kiểm tra OTP
        String clientKey = getClientKey(request);
        String savedOtp = redisTemplate.opsForValue().get(clientKey + ":otp");

        if (savedOtp != null && savedOtp.equals(otpCode)) {
            // Xác thực thành công
            User user = userRepository.findByPhoneNumber(phone);
            if (user != null) {
                user.setActive(true);
                userRepository.save(user);
                redisTemplate.delete(clientKey + ":otp"); // Xóa OTP sau khi xác thực
                return new MessageResponse("alert-success", "Tài khoản đã được kích hoạt thành công!");
            }
        }
        return new MessageResponse("alert-danger", "Mã OTP không chính xác.");
    }
}
