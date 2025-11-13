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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters")
    @Column(unique = true)
    private String userName;

    @NotBlank
    @Size(min = 5, max = 50, message = "Email must be between {min} and {max} characters")
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 10, max = 100, message = "Password must be between {min} and {max} characters")
    private String password;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
        fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role", 
        joinColumns = @JoinColumn(name = "role_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(
        mappedBy = "seller", 
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
        orphanRemoval = true)
    private Set<Product> products;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "user_address", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Set<Address> addresses;

}
