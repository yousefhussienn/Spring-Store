package com.yh.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
