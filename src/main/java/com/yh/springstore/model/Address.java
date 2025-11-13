package com.yh.springstore.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 2, max = 50, message = "Street must be between {min} and {max} characters")
    private String street;

    @Size(min = 2, max = 50, message = "Building Name must be between {min} and {max} characters")
    private String buildingName;
    
    @NotBlank    
    @Size(min = 2, max = 50, message = "City must be between {min} and {max} characters")
    private String city;

    @Size(min = 2, max = 50, message = "State must be between {min} and {max} characters")
    private String state;
    
    @NotBlank
    @Size(min = 2, max = 20, message = "Country must be between {min} and {max} characters")
    private String country;
    
    @Size(min = 6, max = 6, message = "Pincode must have {max} characters")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    // Custom Constructor
    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
    
}
