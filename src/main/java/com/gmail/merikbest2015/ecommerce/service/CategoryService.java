package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.Category;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();

    List<Category> searchCategories(String keyword);

    Category getCategoryById(Long categoryId);
}

