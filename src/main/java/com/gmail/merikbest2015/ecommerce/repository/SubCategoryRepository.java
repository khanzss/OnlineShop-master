package com.gmail.merikbest2015.ecommerce.repository;

import com.gmail.merikbest2015.ecommerce.domain.Category;
import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    // Lấy tất cả subcategories theo categoryId và displayOrder
    List<SubCategory> findAllByVisibleTrueOrderByDisplayOrder();

    // Tìm kiếm subcategory theo từ khóa và hiển thị theo displayOrder
    List<SubCategory> findByNameContainingIgnoreCaseAndVisibleTrueOrderByDisplayOrder(String keyword);
}
