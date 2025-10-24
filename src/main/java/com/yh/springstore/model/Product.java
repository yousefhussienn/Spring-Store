package com.yh.springstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 5, message = "Name must have minimum 5 characters")
    @Size(max = 50, message = "Name must have maximum 50 characters")
    private String productName;
    
    private String imageUrl;

    @NotBlank
    @Size(min = 5, message = "Name must have minimum 5 characters")
    @Size(max = 250, message = "Name must have maximum 250 characters")
    private String description;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Max(value = 100000, message = "Quantity limit exceeded")
    private int quantity;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price value is too high!")
    private double price;

    @DecimalMin(value = "0.01", message = "Discount Percent minimum is 0.01 (1%)")
    @DecimalMax(value = "1.00", message = "Discount Percent maximum is 1.00 (100%)")
    private double discountPercent;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
