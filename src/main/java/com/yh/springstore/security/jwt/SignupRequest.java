package com.yh.springstore.security.jwt;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
    
    @NotBlank
    @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotBlank
    @Size(min = 5, max = 50, message = "Email must be between {min} and {max} characters")
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 100, message = "Password must be between {min} and {max} characters")
    private String password;

    private Set<String> roles;

}
