package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.CreateCategoryRequest;
import com.ecommerce.catalog.entity.Category;
import com.ecommerce.catalog.service.CategoryService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Product category endpoints")
@Slf4j
public class CategoryController {
    
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CreateCategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        
        Category category = categoryService.createCategory(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(category, "Category created successfully"));
    }
    
    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        log.info("Fetching all categories");
        
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        log.info("Fetching category: {}", id);
        
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id,
                                                               @RequestBody CreateCategoryRequest request) {
        log.info("Updating category: {}", id);
        
        Category category = categoryService.updateCategory(id, request.getName(), request.getDescription());
        return ResponseEntity.ok(ApiResponse.success(category, "Category updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category: {}", id);
        
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}
