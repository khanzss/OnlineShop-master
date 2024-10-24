package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.*;
import com.gmail.merikbest2015.ecommerce.dto.request.CategoryRequest;
import com.gmail.merikbest2015.ecommerce.dto.request.PerfumeRequest;
import com.gmail.merikbest2015.ecommerce.dto.request.SearchRequest;
import com.gmail.merikbest2015.ecommerce.dto.request.SubCategoryRequest;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    Page<Perfume> getPerfumes(Pageable pageable);

    Page<Perfume> searchPerfumes(SearchRequest request, Pageable pageable);

    Page<User> getUsers(Pageable pageable);

    Page<User> searchUsers(SearchRequest request, Pageable pageable);

    Order getOrder(Long orderId);

    Page<Order> getOrders(Pageable pageable);

    Page<Order> searchOrders(SearchRequest request, Pageable pageable);

    Perfume getPerfumeById(Long perfumeId);

    MessageResponse editPerfume(PerfumeRequest perfumeRequest, MultipartFile file);

    MessageResponse addPerfume(PerfumeRequest perfumeRequest, MultipartFile file);

    UserInfoResponse getUserById(Long userId, Pageable pageable);


    // Category
    Page<Category> getAllCategories(Pageable pageable);
    MessageResponse addCategory(CategoryRequest categoryRequest);
    MessageResponse editCategory(CategoryRequest categoryRequest);
    MessageResponse deleteCategory(Long id);
    MessageResponse toggleCategoryVisibility(Long id);


    // SubCategory
    Page<SubCategory> getAllSubCategories(Pageable pageable);
    MessageResponse addSubCategory(SubCategoryRequest subCategoryRequest);
    MessageResponse editSubCategory(SubCategoryRequest subCategoryRequest);
    MessageResponse deleteSubCategory(Long id);
    MessageResponse toggleSubCategoryVisibility(Long id);
}
