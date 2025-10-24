package com.yh.springstore.service;

import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;

public interface ProductService {
    ProductResponse getProducts();

    ProductResponse searchProductsByCategory(Long categoryId);

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse searchProductsByKeyword(String keyword);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
}
