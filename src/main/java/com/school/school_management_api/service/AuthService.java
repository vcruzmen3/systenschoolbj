package com.school.school_management_api.service;

import com.school.school_management_api.dto.auth.AuthResponse;
import com.school.school_management_api.dto.auth.LoginRequest;
import com.school.school_management_api.dto.auth.RefreshTokenRequest;
import com.school.school_management_api.entity.ApplicationUser;
import com.school.school_management_api.entity.RefreshToken;
import com.school.school_management_api.repository.RefreshTokenRepository;
import com.school.school_management_api.repository.UserRepository;
import com.school.school_management_api.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);

        ApplicationUser user = userRepository.findWithRolesAndPermissionsByIdentifier(request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RefreshToken refreshToken = createRefreshToken(user);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.replace("ROLE_", ""))
                .toList();

        List<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> !auth.startsWith("ROLE_"))
                .toList();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(tokenProvider.getJwtExpirationMs() / 1000)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token no válido"));

        // Usar el método heler de tu entidad directamente
        if (!refreshToken.isActive()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token expirado o revocado. Inicie sesión de nuevo.");
        }

        ApplicationUser user = refreshToken.getUser();

        List<String> authorities = user.getUserRoles().stream()
                .map(ur -> "ROLE_" + ur.getRole().getName())
                .toList();

        String newAccessToken = tokenProvider.generateTokenFromUsername(user.getUsername(), authorities);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(tokenProvider.getJwtExpirationMs() / 1000)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private RefreshToken createRefreshToken(ApplicationUser user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7)) // <-- LocalDateTime en lugar de Instant
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResourceNotFoundException("Refresh token es requerido.");
        }

        String cleanToken = refreshToken.replaceAll("\\s+", "");
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(cleanToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token no encontrado o inválido"));

        refreshTokenRepository.delete(tokenEntity);
    }
}