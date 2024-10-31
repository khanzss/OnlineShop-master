package com.gmail.merikbest2015.ecommerce.repository;


import com.gmail.merikbest2015.ecommerce.domain.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    // JpaRepository cung cấp sẵn các phương thức cơ bản như findById, save, delete, v.v.
}

