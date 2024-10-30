package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Lấy tất cả categories hiển thị theo displayOrder
    List<Category> findAllByVisibleTrueOrderByDisplayOrder();

    // Tìm kiếm category theo từ khóa và hiển thị theo displayOrder
    List<Category> findByNameContainingIgnoreCaseAndVisibleTrueOrderByDisplayOrder(String keyword);
}

