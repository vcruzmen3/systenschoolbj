package com.school.school_management_api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario o email es obligatorio.")
    private String usernameOrEmail;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
}