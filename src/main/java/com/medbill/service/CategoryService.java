package com.medbill.service;

import java.util.List;

import com.medbill.entity.Category;

public interface CategoryService {

    List<Category> getAllCategories(Long companyId);

    List<Category> getActiveCategories(Long companyId);

    Category getCategoryById(Long id);

    Category createCategory(Category category, Long companyId);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}

