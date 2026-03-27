package com.yh.springstore.service;

import java.util.List;

import com.yh.springstore.payload.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, int quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartForLoggedInUser();

    CartDTO updateProductQuantityInCart(Long productId, int quantity);

    CartDTO deleteProductFromCart(Long productId);

}
