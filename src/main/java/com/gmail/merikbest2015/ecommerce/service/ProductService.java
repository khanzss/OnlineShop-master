package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product getProductById(Long productId);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> searchProducts(SearchRequest request, Pageable pageable);

    Page<Product> getProductsByFilterParams(SearchRequest request, Pageable pageable);

    Page<Product> getPopularProducts(Pageable pageable);
}
