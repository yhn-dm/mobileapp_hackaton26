package com.example.api.dto.request;

import com.example.api.enums.UserRole;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Email
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private UserRole role;
}
