package com.yh.springstore.service;

import com.yh.springstore.payload.ProductDTO;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    
}
