package com.yh.springstore.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    // Response Content (List of Products) 
    private List<ProductDTO> content;
}
