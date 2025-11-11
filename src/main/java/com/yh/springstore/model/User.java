package com.yh.springstore.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(min = 5, message = "Username must have minimum 5 characters")
    @Size(max = 20, message = "Username must have maximum 20 characters")
    private String userName;

    @NotBlank
    @Size(min = 5, message = "Email must have minimum 5 characters")
    @Size(max = 50, message = "Email must have maximum 50 characters")
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, message = "Password must have minimum 10 characters")
    @Size(max = 100, message = "Password must have maximum 100 characters")
    private String password;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
        fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role", 
        joinColumns = @JoinColumn(name = "roleId"), 
        inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<Role> roles = new HashSet<>();
}
