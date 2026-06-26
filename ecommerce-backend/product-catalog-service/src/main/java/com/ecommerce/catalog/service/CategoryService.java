package com.ecommerce.catalog.service;

import com.ecommerce.catalog.entity.Category;
import com.ecommerce.catalog.repository.CategoryRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    public Category createCategory(String name, String description) {
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException("Category already exists with name: " + name, "CATEGORY_EXISTS", 400);
        }
        
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        
        return categoryRepository.save(category);
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category updateCategory(Long id, String name, String description) {
        Category category = getCategoryById(id);
        
        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new BusinessException("Category already exists with name: " + name, "CATEGORY_EXISTS", 400);
        }
        
        category.setName(name);
        category.setDescription(description);
        
        return categoryRepository.save(category);
    }
    
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
