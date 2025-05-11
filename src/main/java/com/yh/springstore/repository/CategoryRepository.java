package com.yh.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    
} 
