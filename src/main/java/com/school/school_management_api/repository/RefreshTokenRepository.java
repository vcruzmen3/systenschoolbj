package com.school.school_management_api.repository;

import com.school.school_management_api.entity.ApplicationUser;
import com.school.school_management_api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    // Buscar un refresh token por la cadena de token
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenIgnoreCase(String token);

    // Eliminar o revocar tokens asociados a un usuario al cerrar sesión o expirar
    @Modifying
    int deleteByUser(ApplicationUser user);

    // Revocar/Eliminar un token específico por su cadena
    @Modifying
    int deleteByToken(String token);
}