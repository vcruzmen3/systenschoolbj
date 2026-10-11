package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El código del permiso es obligatorio.")
    @Size(max = 100, message = "El código no puede exceder los 100 caracteres.")
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @NotBlank(message = "El nombre del permiso es obligatorio.")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres.")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "El módulo es obligatorio.")
    @Size(max = 100, message = "El módulo no puede exceder los 100 caracteres.")
    @Column(name = "module", nullable = false, length = 100)
    private String module;

    // Relación 1:N con la tabla intermedia RolePermission
    @Builder.Default
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<RolePermission> rolePermissions = new ArrayList<>();
}