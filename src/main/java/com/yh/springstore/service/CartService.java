package com.yh.springstore.service;

import com.yh.springstore.payload.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, int quantity);

}
