package com.gmail.merikbest2015.ecommerce.dto.request;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;

    @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private Double totalPrice;

    private LocalDateTime date = LocalDateTime.now();

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String fullName;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String city;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String address;

    @NotBlank(message = ErrorMessage.EMPTY_PHONE_NUMBER)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private String paymentMethod;

    @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private List<OrderItemRequest> orderItems;  // Danh sách các mục trong đơn hàng

    // Lớp lồng nhau để lưu thông tin từng mục hàng trong đơn hàng
    @Data
    public static class OrderItemRequest {
        @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
        private Long productId;

        @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
        private Long productOptionId;

        @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
        @Min(value = 1, message = ErrorMessage.INVALID_QUANTITY)
        private Integer quantity;

        @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
        private BigDecimal price;  // Giá của từng ProductOption tại thời điểm đặt hàng
    }
}
