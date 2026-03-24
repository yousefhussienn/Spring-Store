package com.yh.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.springstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
