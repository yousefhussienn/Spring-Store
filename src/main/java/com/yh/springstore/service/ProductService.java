package com.yh.springstore.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;

public interface ProductService {
    ProductResponse getProducts();

    ProductResponse searchProductsByCategory(Long categoryId);

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO updateProductImage(Long productId, MultipartFile imageFile) throws IOException;
}
