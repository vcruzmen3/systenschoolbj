package com.school.school_management_api.repository;

import com.school.school_management_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Busca un rol por su nombre (ej: "ADMIN").
     * @param name Nombre del rol a consultar.
     * @return Un Optional conteniendo la entidad Role si existe.
     */
    Optional<Role> findByName(String name);

    /**
     * Verifica si existe un rol por su nombre.
     * @param name Nombre del rol.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByName(String name);
}