package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getSubCategories();

    List<SubCategory> searchSubCategories(String keyword);

    SubCategory getSubCategoryById(Long subCategoryId);
}