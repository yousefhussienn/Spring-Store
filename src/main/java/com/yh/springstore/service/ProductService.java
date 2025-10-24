package com.yh.springstore.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;

public interface ProductService {
    ProductResponse getProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO updateProductImage(Long productId, MultipartFile imageFile) throws IOException;
}
