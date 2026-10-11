package com.school.school_management_api.controller;

import com.school.school_management_api.dto.auth.AuthResponse;
import com.school.school_management_api.dto.auth.LoginRequest;
import com.school.school_management_api.dto.auth.RefreshTokenRequest;
import com.school.school_management_api.dto.request.LogoutRequest;
import com.school.school_management_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión de usuario", description = "Invalida y elimina el Refresh Token del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sesión cerrada exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Access Token inválido o expirado")
    })
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build(); // Retorna HTTP 204 No Content
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}