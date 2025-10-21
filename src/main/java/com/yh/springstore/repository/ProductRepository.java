package com.yh.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
