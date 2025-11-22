package com.yh.springstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yh.springstore.model.Role;
import com.yh.springstore.model.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByRoleName(UserRole roleAdmin);
 
}
