package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Size(max = 50, message = "El nombre del rol no puede exceder los 50 caracteres.")
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    @Column(name = "description", length = 255)
    private String description;

    // Relación 1:N con la tabla intermedia UserRole (Cambiado a Set)
    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserRole> userRoles = new HashSet<>();

    // Relación 1:N con la tabla intermedia RolePermission (Cambiado a Set)
    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<RolePermission> rolePermissions = new HashSet<>();

    // --- Métodos Helper para sincronizar relaciones bidireccionales ---

    public void addRolePermission(RolePermission rolePermission) {
        rolePermissions.add(rolePermission);
        rolePermission.setRole(this);
    }

    public void removeRolePermission(RolePermission rolePermission) {
        rolePermissions.remove(rolePermission);
        rolePermission.setRole(null);
    }

    public void addUserRole(UserRole userRole) {
        userRoles.add(userRole);
        userRole.setRole(this);
    }

    public void removeUserRole(UserRole userRole) {
        userRoles.remove(userRole);
        userRole.setRole(null);
    }

    // --- Implementación segura de equals & hashCode para JPA ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}