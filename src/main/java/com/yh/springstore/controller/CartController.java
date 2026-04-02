package com.yh.springstore.controller;

import org.springframework.web.bind.annotation.RestController;

import com.yh.springstore.payload.CartDTO;
import com.yh.springstore.service.CartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable int quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
    
    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getMyCart() {
        CartDTO cart = cartService.getCartForLoggedInUser();
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateCartProduct(
            @PathVariable Long productId,
            @PathVariable int quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/carts/products/{productId}")
    public ResponseEntity<CartDTO> deleteProductFromCart(
            @PathVariable Long productId) {
        CartDTO cartDTO = cartService.deleteProductFromCart(productId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
}
