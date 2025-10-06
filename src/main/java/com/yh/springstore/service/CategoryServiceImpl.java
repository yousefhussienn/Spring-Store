package com.yh.springstore.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public CategoryResponse getCategories(int pageNumber, int pageSize) {
        
        // Fetch categories from the database - with Pagination -
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoriesPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoriesPage.getContent();
        
        // Check if no categories returned
        if (categories.isEmpty())
            throw new APIException("No Categories created yet !");

        // Map Category entities to DTOs using ModelMapper
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        // Set Category Response object and return
        CategoryResponse categoryResponse = new CategoryResponse(
            categoryDTOs,
            categoriesPage.getNumber(),
            categoriesPage.getSize(),
            categoriesPage.getTotalElements(),
            categoriesPage.getTotalPages(),
            categoriesPage.isLast()
        );
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // Map the incoming CategoryDTO to a Category entity
        Category newCategory = modelMapper.map(categoryDTO, Category.class);

        // Check if category with same name already exists
        if (categoryRepository.findByCategoryName(newCategory.getCategoryName()) != null)
            throw new APIException("Category with name " + newCategory.getCategoryName() + " already exists !");

        // Save Category in DB
        newCategory.setCategoryId(null); // ignore id if sent (id is auto generated)
        newCategory = categoryRepository.save(newCategory);
        
        // Map the saved Category entity back to a DTO and return
        CategoryDTO newCategoryDTO = modelMapper.map(newCategory, CategoryDTO.class);
        return newCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
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

        // Map the deleted Category entity to a DTO and return
        CategoryDTO categoryDTO = modelMapper.map(existingCategory, CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // Map the incoming CategoryDTO to a Category entity
        Category newCategory = modelMapper.map(categoryDTO, Category.class);

        // Check new category object is not null
        if (newCategory.equals(null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input: Provide new Category data!");
        // Check new category name is not empty
        else if (newCategory.getCategoryName() == null || newCategory.getCategoryName().isEmpty())
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

        // Update Category with the new values
        existingCategory.setCategoryName(newCategory.getCategoryName());
        newCategory = categoryRepository.save(existingCategory);
        
        // Map the updated Category entity to a DTO and return
        CategoryDTO newCategoryDTO = modelMapper.map(newCategory, CategoryDTO.class);
        return newCategoryDTO;
    }

}
