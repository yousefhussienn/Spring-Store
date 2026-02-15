package com.yh.springstore.security.jwt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private List<String> roles;
    private String jwtToken;

    public UserInfoResponse(Long id2, String username2, List<String> roles2) {
        id = id2;
        username = username2;
        roles = roles2;
    }
}


