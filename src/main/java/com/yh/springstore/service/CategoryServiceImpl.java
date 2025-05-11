package com.yh.springstore.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yh.springstore.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public boolean addCategory(Category category) {
        category.setCategoryId(nextId++);
        return categories.add(category);
    }

    @Override
    public String deleteCategory(Long id) {
        // Get category need to be deleted
        Category category = categories
                .stream()
                .filter(cat -> cat.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category with id ( " + id + " ) not Found !"));
        
        // Delete category
        categories.remove(category);
        return "Category with id ( " + id + " ) deleted successfully";
    }

    @Override
    public String updateCategory(Long id, Category newCategory) {
        // Check new category object is not null 
        if(newCategory.equals(null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Provide new Category data!");
        // Check new category name is not empty
        if(newCategory.getCategoryName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Category name required!");

        // Get category need to be updated
        Category category = categories
                .stream()
                .filter(cat -> cat.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category with id ( " + id + " ) not Found !"));
        
        // Update category values
        category.setCategoryName(newCategory.getCategoryName());
        return "Category with id ( " + id + " ) updated successfully";
    }

}
