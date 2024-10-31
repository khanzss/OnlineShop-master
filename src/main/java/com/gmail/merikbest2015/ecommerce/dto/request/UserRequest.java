package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserRequest {

    @NotBlank(message = "Họ và tên không được để trống.")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải có 10 chữ số.")
    private String phone;

    @NotNull(message = "Ngày tháng năm sinh không được để trống.")
    private LocalDate birthDate;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự.")
    private String password;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu.")
    private String password2;
}
