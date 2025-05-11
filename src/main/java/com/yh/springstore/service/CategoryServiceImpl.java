package com.yh.springstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yh.springstore.model.Category;
import com.yh.springstore.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        List<Category> categories = categoryRepository.findAll();

        // Get category need to be deleted
        Category category = categories
                .stream()
                .filter(cat -> cat.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category with id ( " + id + " ) not Found !"));
        
        // Delete category
        categoryRepository.delete(category);
        return "Category with id ( " + id + " ) deleted successfully";
    }

    @Override
    public String updateCategory(Long id, Category newCategory) {
        // Check new category object is not null 
        if(newCategory.equals(null)) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Provide new Category data!");
        // Check new category name is not empty
        if(newCategory.getCategoryName() == null || newCategory.getCategoryName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Category name required!");

        List<Category> categories = categoryRepository.findAll();

        // Get existing category need to be updated
        Category existingCategory = categories
                .stream()
                .filter(cat -> cat.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category with id ( " + id + " ) not Found !"));
        
        // Update category values
        existingCategory.setCategoryName(newCategory.getCategoryName());
        categoryRepository.save(existingCategory);
        return "Category with id ( " + id + " ) updated successfully";
    }

}
