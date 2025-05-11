package com.yh.springstore.service;

import java.util.List;

import com.yh.springstore.model.Category;

public interface CategoryService {
    List<Category> getCategories();

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
