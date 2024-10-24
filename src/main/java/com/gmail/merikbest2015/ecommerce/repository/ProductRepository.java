package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.popular = true")
    Page<Product> findByPopularTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(coalesce(:subCategories, null) IS NULL OR p.subCategory.name IN :subCategories) " +
            "AND (coalesce(:priceStart, null) IS NULL OR p.price BETWEEN :priceStart AND :priceEnd) " +
            "AND (:popular IS NULL OR p.popular = :popular) " +
            "ORDER BY p.price ASC")
    Page<Product> getProductsByFilterParams(
            List<String> subCategories,
            Integer priceStart,
            Integer priceEnd,
            Boolean popular,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'productTitle' THEN UPPER(p.productTitle) " +
            "   ELSE UPPER(p.shortDescription) " +
            "END) LIKE UPPER(CONCAT('%',:text,'%'))")
    Page<Product> searchProducts(String searchType, String text, Pageable pageable);
}