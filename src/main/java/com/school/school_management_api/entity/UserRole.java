package com.school.school_management_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRole {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private Role role;

    // Constructor de conveniencia
    public UserRole(ApplicationUser user, Role role) {
        this.user = user;
        this.role = role;
        if (user != null && role != null) {
            this.id = new UserRoleId(user.getId(), role.getId());
        }
    }
}