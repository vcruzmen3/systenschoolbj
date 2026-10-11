package com.school.school_management_api.dto.auth;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn; // Tiempo en milisegundos o segundos

    // Información del Usuario autenticado
    private UUID userId;
    private String username;
    private String email;
    private List<String> roles;
    private List<String> permissions;
}