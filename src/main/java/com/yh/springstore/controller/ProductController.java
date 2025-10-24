package com.yh.springstore.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yh.springstore.config.AppConstants;
import com.yh.springstore.payload.CategoryDTO;
import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;
import com.yh.springstore.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getProducts(
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.searchProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.searchProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile imageFile) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, imageFile);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
