package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 100)
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @NotBlank
    @Size(max = 30)
    @Builder.Default
    @Column(nullable = false, length = 30)
    private String role = "User";

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // Cambiado de List a Set para evitar MultipleBagFetchException
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    // Cambiado de List a Set para solucionar el error al traer UserRole -> Role -> Permission
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    // Relaciones 1:1 con Perfiles de Rol
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student studentProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher teacherProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StaffProfile staffProfile;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // --- Métodos Helper para sincronizar relaciones bidireccionales ---

    public void addUserRole(UserRole userRole) {
        userRoles.add(userRole);
        userRole.setUser(this);
    }

    public void removeUserRole(UserRole userRole) {
        userRoles.remove(userRole);
        userRole.setUser(null);
    }

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setUser(this);
    }

    public void removeRefreshToken(RefreshToken refreshToken) {
        refreshTokens.remove(refreshToken);
        refreshToken.setUser(null);
    }

    // --- Implementación segura de equals & hashCode para entidades JPA ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}