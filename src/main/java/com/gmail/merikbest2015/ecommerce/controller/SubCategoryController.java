package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import com.gmail.merikbest2015.ecommerce.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    // Get all visible subcategories with pagination
    @GetMapping
    public Page<SubCategory> getSubCategories(Pageable pageable) {
        return subCategoryService.getSubCategories(pageable);
    }

    // Search subcategories by name with pagination
    @GetMapping("/search")
    public Page<SubCategory> searchSubCategories(@RequestParam("keyword") String keyword, Pageable pageable) {
        return subCategoryService.searchSubCategories(keyword, pageable);
    }

    // Get a specific subcategory by ID
    @GetMapping("/{id}")
    public SubCategory getSubCategoryById(@PathVariable Long id) {
        return subCategoryService.getSubCategoryById(id);
    }
}
