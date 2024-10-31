package com.gmail.merikbest2015.ecommerce.dto.request;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EditUserRequest {

    @NotBlank(message = ErrorMessage.EMPTY_FULL_NAME)
    private String fullName;

    @NotNull(message = ErrorMessage.EMPTY_BIRTH_DATE)
    private LocalDate birthDate;

    private String city;
    private String address;

    @NotBlank(message = ErrorMessage.EMPTY_PHONE_NUMBER)
    private String phoneNumber;
}
