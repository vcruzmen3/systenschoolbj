package com.school.school_management_api.repository;

import com.school.school_management_api.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

    // Buscar usuario por username o email para el inicio de sesión
    Optional<ApplicationUser> findByUsernameOrEmail(String username, String email);

    // Comprobar existencia previa de username o email al registrar
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Consulta optimizada para cargar el usuario con sus roles y permisos en una sola consulta
    @Query("SELECT DISTINCT u FROM ApplicationUser u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role r " +
            "LEFT JOIN FETCH r.rolePermissions rp " +
            "LEFT JOIN FETCH rp.permission " +
            "WHERE u.username = :identifier OR u.email = :identifier")
    Optional<ApplicationUser> findWithRolesAndPermissionsByIdentifier(@Param("identifier") String identifier);
}