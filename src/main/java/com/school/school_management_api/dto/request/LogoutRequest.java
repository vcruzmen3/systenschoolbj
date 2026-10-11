package com.school.school_management_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {

    @NotBlank(message = "El refresh token es obligatorio para cerrar sesión")
    private String refreshToken;
}