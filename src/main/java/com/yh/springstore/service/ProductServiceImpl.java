package com.yh.springstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Category;
import com.yh.springstore.model.Product;
import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.repository.CategoryRepository;
import com.yh.springstore.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        // Get the new Product Category by ID -if exists-
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // Map the incoming ProductDTO to a Product entity
        Product newProduct = modelMapper.map(productDTO, Product.class);

        // Associate the new Product with the found Category
        newProduct.setCategory(existingCategory);

        // Save Category in DB
        newProduct.setProductId(null); // ignore id if sent (id is auto generated)
        newProduct = productRepository.save(newProduct);

        // Map the saved Product entity back to a DTO and return
        ProductDTO newProductDTO = modelMapper.map(newProduct, ProductDTO.class);
        return newProductDTO;
    }

}
