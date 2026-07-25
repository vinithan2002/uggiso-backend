package com.uggiso.uggiso_backend.dto.response;

import com.uggiso.uggiso_backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String userName;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String profileImage;

    private Role role;

}