package com.yh.springstore.service;

import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.payload.ProductResponse;

public interface ProductService {
    ProductResponse getProducts();

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
}
