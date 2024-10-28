package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.constants.SuccessMessage;
import com.gmail.merikbest2015.ecommerce.domain.*;
import com.gmail.merikbest2015.ecommerce.domain.product.Product;
import com.gmail.merikbest2015.ecommerce.domain.product.ProductImage;
import com.gmail.merikbest2015.ecommerce.domain.product.ReviewImage;
import com.gmail.merikbest2015.ecommerce.dto.request.*;
import com.gmail.merikbest2015.ecommerce.dto.response.MessageResponse;
import com.gmail.merikbest2015.ecommerce.dto.response.UserInfoResponse;
import com.gmail.merikbest2015.ecommerce.repository.*;
import com.gmail.merikbest2015.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Perfume> getPerfumes(Pageable pageable) {
        return perfumeRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Perfume> searchPerfumes(SearchRequest request, Pageable pageable) {
        return perfumeRepository.searchPerfumes(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(SearchRequest request, Pageable pageable) {
        return userRepository.searchUsers(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);

    }

    @Override
    public Page<Order> searchOrders(SearchRequest request, Pageable pageable) {
        return orderRepository.searchOrders(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Perfume getPerfumeById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PERFUME_NOT_FOUND));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editPerfume(PerfumeRequest perfumeRequest, MultipartFile file) {
        return savePerfume(perfumeRequest, file, SuccessMessage.PERFUME_EDITED);
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addPerfume(PerfumeRequest perfumeRequest, MultipartFile file) {
        return savePerfume(perfumeRequest, file, SuccessMessage.PERFUME_ADDED);
    }

    @Override
    public UserInfoResponse getUserById(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findOrderByUserId(userId, pageable);
        return new UserInfoResponse(user, orders);
    }

    private MessageResponse savePerfume(PerfumeRequest perfumeRequest, MultipartFile file, String message) throws IOException {
        Perfume perfume = modelMapper.map(perfumeRequest, Perfume.class);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            perfume.setFilename(resultFilename);
        }
        perfumeRepository.save(perfume);
        return new MessageResponse("alert-success", message);
    }









    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    // Category Services
    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public MessageResponse addCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDisplayOrder(categoryRequest.getDisplayOrder());
        category.setVisible(categoryRequest.getVisible());

        categoryRepository.save(category);
        return new MessageResponse("alert-success", "Category added successfully.");
    }

    @Override
    public MessageResponse editCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryRequest.getName());
        category.setDisplayOrder(categoryRequest.getDisplayOrder());
        category.setVisible(categoryRequest.getVisible());

        categoryRepository.save(category);
        return new MessageResponse("alert-success", "Category updated successfully.");
    }

    @Override
    public MessageResponse deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return new MessageResponse("alert-warning", "Category not found.");
        }
        categoryRepository.deleteById(id);
        return new MessageResponse("alert-success", "Category deleted successfully.");
    }

    @Override
    public MessageResponse toggleCategoryVisibility(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setVisible(!category.isVisible());
        categoryRepository.save(category);

        String status = category.isVisible() ? "visible" : "hidden";
        return new MessageResponse("alert-success", "Category is now " + status + ".");
    }






    // SubCategory Services
    @Override
    public Page<SubCategory> getAllSubCategories(Pageable pageable) {
        return subCategoryRepository.findAll(pageable);
    }

    @Override
    public MessageResponse addSubCategory(SubCategoryRequest subCategoryRequest) {
        Category category = categoryRepository.findById(subCategoryRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryRequest.getName());
        subCategory.setDisplayOrder(subCategoryRequest.getDisplayOrder());
        subCategory.setVisible(subCategoryRequest.getVisible());
        subCategory.setCategory(category);  // Liên kết đúng với Category đã có

        subCategoryRepository.save(subCategory);
        return new MessageResponse("alert-success", "SubCategory added successfully.");
    }

    @Override
    public MessageResponse editSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryRequest.getId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Category category = categoryRepository.findById(subCategoryRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        subCategory.setName(subCategoryRequest.getName());
        subCategory.setDisplayOrder(subCategoryRequest.getDisplayOrder());
        subCategory.setVisible(subCategoryRequest.getVisible());
        subCategory.setCategory(category);  // Cập nhật liên kết đúng

        subCategoryRepository.save(subCategory);
        return new MessageResponse("alert-success", "SubCategory updated successfully.");
    }


    @Override
    public MessageResponse deleteSubCategory(Long id) {
        if (!subCategoryRepository.existsById(id)) {
            return new MessageResponse("alert-warning", "SubCategory not found.");
        }
        subCategoryRepository.deleteById(id);
        return new MessageResponse("alert-success", "SubCategory deleted successfully.");
    }

    @Override
    public MessageResponse toggleSubCategoryVisibility(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        subCategory.setVisible(!subCategory.isVisible());
        subCategoryRepository.save(subCategory);

        String status = subCategory.isVisible() ? "visible" : "hidden";
        return new MessageResponse("alert-success", "SubCategory is now " + status + ".");
    }


    //Product
    private final ProductRepository productRepository;
//    private final ModelMapper modelMapper;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addProduct(ProductRequest productRequest, MultipartFile file) {
        Product product = modelMapper.map(productRequest, Product.class);
        setSubCategory(product, productRequest.getSubCategoryId());
        handleFileUpload(product, file);
        productRepository.save(product);
        return new MessageResponse("alert-success", "Product added successfully.");
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editProduct(ProductRequest productRequest, MultipartFile file) {
        Product existingProduct = getProductById(productRequest.getId());
        modelMapper.map(productRequest, existingProduct);
        setSubCategory(existingProduct, productRequest.getSubCategoryId());
        handleFileUpload(existingProduct, file);
        productRepository.save(existingProduct);
        return new MessageResponse("alert-success", "Product edited successfully.");
    }

    @Override
    @Transactional
    public MessageResponse deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
        return new MessageResponse("alert-success", "Product deleted successfully.");
    }

    @Override
    @Transactional
    public MessageResponse toggleProductPopularity(Long productId) {
        Product product = getProductById(productId);
        product.setPopular(!product.isPopular());
        productRepository.save(product);
        String status = product.isPopular() ? "marked as popular" : "unmarked as popular";
        return new MessageResponse("alert-success", "Product " + status + " successfully.");
    }

    private void setSubCategory(Product product, Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubCategory not found"));
        product.setSubCategory(subCategory);
    }

    private void handleFileUpload(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            product.getImages().add(new ProductImage(resultFilename, product)); // Lưu đường dẫn ảnh vào danh sách image
        }
    }

}
