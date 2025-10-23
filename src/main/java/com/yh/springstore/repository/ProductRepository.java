package com.yh.springstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.Category;
import com.yh.springstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findByCategory(Category existingCategory);
    
}
