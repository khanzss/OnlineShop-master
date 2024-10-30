package com.gmail.merikbest2015.ecommerce.controller;

import com.gmail.merikbest2015.ecommerce.domain.SubCategory;
import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.service.ProductService;
import com.gmail.merikbest2015.ecommerce.service.SubCategoryService;
import com.gmail.merikbest2015.ecommerce.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SubCategoryService subCategoryService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product"; // Giao diện chi tiết sản phẩm
    }

//    @GetMapping
//    public String getAllProducts(Pageable pageable, Model model) {
//        Page<Product> products = productService.getAllProducts(pageable);
//        List<SubCategory> subCategories = subCategoryService.getSubCategories(); // Load subcategories
//        model.addAttribute("page", products);
//        model.addAttribute("subCategories", subCategories);
//        return "products";
//    }

    @PostMapping("/search")
    public String searchProducts(@RequestBody SearchRequest request, Pageable pageable, Model model) {
        Page<Product> products = productService.searchProducts(request, pageable);
        model.addAttribute("page", products);
        model.addAttribute("searchRequest", request);
        return "products";
    }

    @GetMapping
    public String getProductsByFilterParams(
            @RequestParam(value = "subCategories", required = false) List<String> subCategories,
            @RequestParam(value = "priceStart", required = false, defaultValue = "0") BigDecimal priceStart,
            @RequestParam(value = "priceEnd", required = false, defaultValue = "1000") BigDecimal priceEnd,
//            @RequestParam(value = "popular", required = false, defaultValue = "false") Boolean popular,
            Pageable pageable, Model model) {
        // Tạo SearchRequest từ các query parameters
        SearchRequest request = new SearchRequest();
        request.setSubCategories(subCategories);
        request.setPriceStart(priceStart);
        request.setPriceEnd(priceEnd);
//        request.setPopular(popular);

        // Gọi service để lấy sản phẩm theo bộ lọc
        Page<Product> products = productService.getProductsByFilterParams(request, pageable);
        System.out.println("Number of products returned: " + products.getContent().size());
        List<SubCategory> subCategoriesList = subCategoryService.getSubCategories();

        // Thêm dữ liệu vào model để truyền sang view
        model.addAttribute("page", products);
        model.addAttribute("searchRequest", request);
        model.addAttribute("subCategories", subCategoriesList);

        return "products";  // Trả về giao diện products.html
    }


    @GetMapping("/popular")
    public String getPopularProducts(Pageable pageable, Model model) {
        Page<Product> products = productService.getPopularProducts(pageable);
        model.addAttribute("page", products);
        return "products";
    }
}
