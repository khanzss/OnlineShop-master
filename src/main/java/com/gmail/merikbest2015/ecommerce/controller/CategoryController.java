package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.domain.Category;
import com.gmail.merikbest2015.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("page", categories);
        return "category-list"; // Giao diện danh sách category
    }

    @GetMapping("/search")
    public String searchCategories(@RequestParam("keyword") String keyword, Model model) {
        List<Category> categories = categoryService.searchCategories(keyword);
        model.addAttribute("page", categories);
        model.addAttribute("keyword", keyword);
        return "category-list";
    }

    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category-detail"; // Giao diện chi tiết category
    }
}
