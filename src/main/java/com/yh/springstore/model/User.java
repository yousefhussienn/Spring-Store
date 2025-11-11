package com.yh.springstore.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(min = 5, message = "Username must have minimum 5 characters")
    @Size(max = 20, message = "Username must have maximum 20 characters")
    @Column(unique = true)
    private String userName;

    @NotBlank
    @Size(min = 5, message = "Email must have minimum 5 characters")
    @Size(max = 50, message = "Email must have maximum 50 characters")
    @Email
    @Column(unique = true)
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
        joinColumns = @JoinColumn(name = "role_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(
        mappedBy = "seller",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
        orphanRemoval = true)
    private Set<Product> products;
}
