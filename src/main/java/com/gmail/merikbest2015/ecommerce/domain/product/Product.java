package com.gmail.merikbest2015.ecommerce.domain.product;

import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import lombok.Data;
//import org.springframework.data.annotation.Id;
import javax.persistence.Id;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productTitle;
    private int quantity;
    private int sold;
    private boolean popular;

    @Column(length = 500)
    private String shortDescription;

    @Lob
    private String detailedDescription;

    @Lob
    private String faqs; // FAQs lưu dưới dạng văn bản

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public BigDecimal getStartingPrice() {
        // Trả về giá option đầu tiên hoặc 0 nếu không có option
        return options.isEmpty() ? BigDecimal.ZERO : options.get(0).getPrice();
    }
}
