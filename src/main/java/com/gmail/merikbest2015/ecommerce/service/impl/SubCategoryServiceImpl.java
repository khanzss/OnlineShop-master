package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.domain.Category;
import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import com.gmail.merikbest2015.ecommerce.repository.CategoryRepository;
import com.gmail.merikbest2015.ecommerce.repository.SubCategoryRepository;
import com.gmail.merikbest2015.ecommerce.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    public List<SubCategory> getSubCategories() {
        return subCategoryRepository.findAllByVisibleTrueOrderByDisplayOrder();
    }

    @Override
    public List<SubCategory> searchSubCategories(String keyword) {
        return subCategoryRepository.findByNameContainingIgnoreCaseAndVisibleTrueOrderByDisplayOrder(keyword);
    }

    @Override
    public SubCategory getSubCategoryById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
    }
}