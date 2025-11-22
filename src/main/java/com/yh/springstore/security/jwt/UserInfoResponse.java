package com.yh.springstore.security.jwt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String jwtToken;
    private List<String> roles;
}


