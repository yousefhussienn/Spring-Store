package com.yh.springstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.Category;
import com.yh.springstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

    Page<Product> findByCategory(Category existingCategory, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String string, Pageable pageable);
    
}
