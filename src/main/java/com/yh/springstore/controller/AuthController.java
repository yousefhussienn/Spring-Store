package com.yh.springstore.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Role;
import com.yh.springstore.model.User;
import com.yh.springstore.model.UserRole;
import com.yh.springstore.payload.APIResponse;
import com.yh.springstore.repository.RoleRepository;
import com.yh.springstore.repository.UserRepository;
import com.yh.springstore.security.jwt.JwtUtils;
import com.yh.springstore.security.jwt.LoginRequest;
import com.yh.springstore.security.jwt.SignupRequest;
import com.yh.springstore.security.jwt.UserInfoResponse;
import com.yh.springstore.security.services.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse loginResponse = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                jwtToken,
                roles);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            return ResponseEntity
                    .badRequest()
                    .body(new APIResponse("Username already exists!", false));

        if (userRepository.existsByEmail(request.getEmail()))
            return ResponseEntity
                    .badRequest()
                    .body(new APIResponse("Email already exists!", false));

        User newUser = new User();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        request.getRoles().forEach(role -> {
            if(role == "admin")
                roles.add(roleRepository
                .findByRoleName(UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", UserRole.ROLE_ADMIN.toString()))
                );
            else if (role == "seller")
                roles.add(roleRepository
                .findByRoleName(UserRole.ROLE_SELLER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", UserRole.ROLE_SELLER.toString()))
                );
            else
                roles.add(roleRepository
                .findByRoleName(UserRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", UserRole.ROLE_USER.toString()))
                );
        });
        newUser.setRoles(roles);

        userRepository.save(newUser);
        
        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
}
