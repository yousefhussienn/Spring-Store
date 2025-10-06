package com.yh.springstore.service;

import com.yh.springstore.model.Category;
import com.yh.springstore.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getCategories();

    Category addCategory(Category category);

    String deleteCategory(Long id);

    String updateCategory(Long id, Category category);

    // String deleteCategory(Long id);

    // Category getCategoryById(Long id);

    // Category updateCategory(Long id, Category category);

    // List<Category> searchCategories(String keyword);

    // List<Category> getCategoriesByParentId(Long parentId);

    // List<Category> getCategoriesByLevel(int level);

    // List<Category> getCategoriesByType(String type);

    // List<Category> getCategoriesByStatus(String status);

    // List<Category> getCategoriesByName(String name);

    // List<Category> getCategoriesByDescription(String description);
}
