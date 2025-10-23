package com.yh.springstore.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String imageUrl;
    private String description;

    private int quantity;
    private double price;
    private double discountPercent;
    
}
