package com.uggiso.uggiso_backend.dto.response;

import com.uggiso.uggiso_backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long userId;

    private String username;

    private String email;

    private Role role;

    private Long restaurantId;

    private String token;

    public LoginResponse(Long id, String username, String email, Role role, String token) {
    }
}