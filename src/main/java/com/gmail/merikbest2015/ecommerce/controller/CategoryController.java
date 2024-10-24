package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.dto.request.CategoryRequest;
import com.gmail.merikbest2015.ecommerce.service.AdminService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final AdminService adminService;
    private final ControllerUtils controllerUtils;

    @GetMapping
    public String getCategories(Pageable pageable, Model model) {
        controllerUtils.addPagination(model, adminService.getAllCategories(pageable));
        return "admin_categories";
    }

    @PostMapping("/add")
    public String addCategory(@Valid @RequestBody CategoryRequest categoryRequest,
                              BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "admin_add_category";
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/categories",
                adminService.addCategory(categoryRequest));
    }

    @PostMapping("/edit")
    public String editCategory(@Valid @RequestBody CategoryRequest categoryRequest,
                               BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "admin_edit_category";
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/categories",
                adminService.editCategory(categoryRequest));
    }

    @PostMapping("/toggle/{id}")
    public String toggleCategoryVisibility(@PathVariable Long id) {
        adminService.toggleCategoryVisibility(id);
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
