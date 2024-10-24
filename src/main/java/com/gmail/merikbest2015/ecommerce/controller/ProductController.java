package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @PostMapping("/search")
    public Page<Product> searchProducts(@RequestBody SearchRequest request, Pageable pageable) {
        return productService.searchProducts(request, pageable);
    }

    @PostMapping("/filter")
    public Page<Product> getProductsByFilterParams(@RequestBody SearchRequest request, Pageable pageable) {
        return productService.getProductsByFilterParams(request, pageable);
    }

    @GetMapping("/popular")
    public Page<Product> getPopularProducts(Pageable pageable) {
        return productService.getPopularProducts(pageable);
    }
}
