package com.yh.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yh.springstore.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
