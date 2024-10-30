package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private Long id; // ID cho cập nhật hoặc sửa
    private String productTitle;
    private int quantity;
    private String shortDescription;
    private String detailedDescription;
    private String faqs;
    private Long subCategoryId;
//    private boolean popular;
    private List<OptionRequest> options;
    private List<String> imageUrls;
}

