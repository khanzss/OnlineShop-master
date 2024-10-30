package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import com.gmail.merikbest2015.ecommerce.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping
    public String getSubCategories(Model model) {
        List<SubCategory> subCategories = subCategoryService.getSubCategories();
        model.addAttribute("page", subCategories);
        return "products"; // Giao diện danh sách subcategory
    }

    @GetMapping("/search")
    public String searchSubCategories(@RequestParam("keyword") String keyword, Model model) {
        List<SubCategory> subCategories = subCategoryService.searchSubCategories(keyword);
        model.addAttribute("page", subCategories);
        model.addAttribute("keyword", keyword);
        return "subcategory-list";
    }

    @GetMapping("/{id}")
    public String getSubCategoryById(@PathVariable Long id, Model model) {
        SubCategory subCategory = subCategoryService.getSubCategoryById(id);
        model.addAttribute("subCategory", subCategory);
        return "subcategory-detail"; // Giao diện chi tiết subcategory
    }
}
