package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OptionRequest {
    private Long id; // ID cho cập nhật hoặc sửa
    private String optionName;
    private BigDecimal price;
}
