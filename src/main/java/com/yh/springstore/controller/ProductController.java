package com.yh.springstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yh.springstore.service.ProductService;

@RestController
@RequestMapping("/someshit")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    
}
