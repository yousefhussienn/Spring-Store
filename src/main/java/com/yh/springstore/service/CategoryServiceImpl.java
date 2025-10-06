package com.yh.springstore.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yh.springstore.exception.APIException;
import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Category;
import com.yh.springstore.payload.CategoryDTO;
import com.yh.springstore.payload.CategoryResponse;
import com.yh.springstore.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty())
            throw new APIException("No Categories created yet !");

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);

        return categoryResponse;
    }

    @Override
    public Category addCategory(Category category) {
        // Check if category with same name exists
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null)
            throw new APIException("Category with name " + category.getCategoryName() + " already exists !");

        category.setCategoryId(null); // ignore id if sent (id is auto generated)
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

        // ** BELOW COMMENTED IS THE OLD WAY FOR REFERENCE **
        // List<Category> categories = categoryRepository.findAll();
        // Category existingCategory = categories
        // .stream()
        // .filter(cat -> cat.getCategoryId().equals(id))
        // .findFirst()
        // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        // "Category with id ( " + id + " ) not Found !"));

        // Delete category
        categoryRepository.delete(existingCategory);
        return "Category with id ( " + id + " ) deleted successfully";
    }

    @Override
    public String updateCategory(Long id, Category newCategory) {
        // Check new category object is not null
        if (newCategory.equals(null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Provide new Category data!");
        // Check new category name is not empty
        if (newCategory.getCategoryName() == null || newCategory.getCategoryName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Category name required!");

        // Get from DB the existing category needs to be updated
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

        // ** BELOW COMMENTED IS THE OLD WAY FOR REFERENCE **
        // List<Category> categories = categoryRepository.findAll();
        // Category existingCategory = categories
        // .stream()
        // .filter(cat -> cat.getCategoryId().equals(id))
        // .findFirst()
        // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        // "Category with id ( " + id + " ) not Found !"));

        // Update category values
        existingCategory.setCategoryName(newCategory.getCategoryName());
        categoryRepository.save(existingCategory);
        return "Category with id ( " + id + " ) updated successfully";
    }

}
