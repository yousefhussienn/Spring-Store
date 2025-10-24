package com.yh.springstore.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yh.springstore.exception.APIException;
import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Category;
import com.yh.springstore.model.Product;
import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;
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

    @Autowired
    private FileService fileService;

    @Override
    public ProductResponse getProducts() {
        
        // Fetch products from the database
        List<Product> products = productRepository.findAll();
        
        // Check if no products returned
        if (products.isEmpty())
            throw new APIException("No Categories created yet !");

        // Map Product entities to DTOs using ModelMapper
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        
        // Set Product Response object and return
        ProductResponse productResponse = new ProductResponse(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByCategory(Long categoryId) {
        // Get the new Product Category by ID -if exists-
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // Fetch products from the database
        List<Product> products = productRepository.findByCategory(existingCategory);
        
        // Check if no products returned
        if (products.isEmpty())
            throw new APIException("No Categories created yet !");

        // Map Product entities to DTOs using ModelMapper
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        // Set Product Response object and return
        ProductResponse productResponse = new ProductResponse(productDTOs);
        return productResponse;
    }

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

    @Override
    public ProductResponse searchProductsByKeyword(String keyword) {
        // Fetch products -with Keyword- from the Database
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");
        
        // Check if no products returned
        if (products.isEmpty())
            throw new APIException("No Products found with the given keyword!");

        // Map Product entities to DTOs using ModelMapper
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        
        // Set Product Response object and return
        ProductResponse productResponse = new ProductResponse(productDTOs);
        return productResponse;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        // Get the Product by ID -if exists-
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Delete Product
        productRepository.delete(existingProduct);

        // Map the deleted Product entity to a DTO and return
        ProductDTO deletedProductDTO = modelMapper.map(existingProduct, ProductDTO.class);
        return deletedProductDTO;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        // Get the Product by ID -if exists-
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Map the incoming ProductDTO to a Product entity
        Product newProduct = modelMapper.map(productDTO, Product.class);
        
        // Update Product with the new values
        if(newProduct.getProductName() != null || !newProduct.getProductName().isEmpty())
            existingProduct.setProductName(newProduct.getProductName());
        if(newProduct.getDescription() != null || !newProduct.getDescription().isEmpty())
            existingProduct.setDescription(newProduct.getDescription());
        if(newProduct.getImageUrl() != null || !newProduct.getImageUrl().isEmpty())
            existingProduct.setImageUrl(newProduct.getImageUrl());

        if(newProduct.getQuantity() != 0)
            existingProduct.setQuantity(newProduct.getQuantity());
        if(newProduct.getPrice() != 0.0)
            existingProduct.setPrice(newProduct.getPrice());
        if(newProduct.getDiscountPercent() != 0.0)
            existingProduct.setDiscountPercent(newProduct.getDiscountPercent());
        
        // Update Product in Database
        productRepository.save(existingProduct);

        // Map the deleted Product entity to a DTO and return
        ProductDTO deletedProductDTO = modelMapper.map(existingProduct, ProductDTO.class);
        return deletedProductDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile imageFile) throws IOException {
        // Get the Product by ID -if exists-
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        
        // Upload image to server
        String path = "images/products/";
        String filename = fileService.uploadImage(path, imageFile);

        // Update Product with the new image file name
        existingProduct.setImageUrl(filename);
        
        // Update Product in Database
        productRepository.save(existingProduct);

        // Map the deleted Product entity to a DTO and return
        ProductDTO deletedProductDTO = modelMapper.map(existingProduct, ProductDTO.class);
        return deletedProductDTO;
    }


}
