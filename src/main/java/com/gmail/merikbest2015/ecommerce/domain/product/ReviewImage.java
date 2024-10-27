package com.gmail.merikbest2015.ecommerce.domain.product;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "review_image")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewImage(String imageUrl, Review review) {
        this.imageUrl = imageUrl;
        this.review = review;
    }
}

