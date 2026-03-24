package com.yh.springstore.payload;

import java.util.ArrayList;
import java.util.List;

import com.yh.springstore.model.CartItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private List<CartItem> cartItems = new ArrayList<>();
    private double totalPrice = 0.0;
}
