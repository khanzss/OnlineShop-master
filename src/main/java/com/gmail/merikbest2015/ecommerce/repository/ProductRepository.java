package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.popular = true")
    Page<Product> findByPopularTrue(Pageable pageable);
    @Query(value = "SELECT p FROM Product p " +
            "JOIN p.options o " +
            "WHERE (:subCategories IS NULL OR p.subCategory.id IN :subCategories) " +
            "AND (o.price BETWEEN :priceStart AND :priceEnd) " +
            "GROUP BY p.id " +
            "ORDER BY MIN(o.price) ASC",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Product p " +
                    "JOIN p.options o " +
                    "WHERE (:subCategories IS NULL OR p.subCategory.id IN :subCategories) " +
                    "AND (o.price BETWEEN :priceStart AND :priceEnd)")
    Page<Product> getProductsByFilterParams(
            List<String> subCategories,
            BigDecimal priceStart,
            BigDecimal priceEnd,
//            Boolean popular,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'productTitle' THEN UPPER(p.productTitle) " +
            "   ELSE UPPER(p.shortDescription) " +
            "END) LIKE UPPER(CONCAT('%',:text,'%'))")
    Page<Product> searchProducts(String searchType, String text, Pageable pageable);
}