package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "StaffProfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "El usuario es obligatorio.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private ApplicationUser user;

    @NotBlank(message = "El departamento es obligatorio.")
    @Size(max = 100, message = "El departamento no puede exceder los 100 caracteres.")
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @NotBlank(message = "El puesto es obligatorio.")
    @Size(max = 100, message = "El puesto no puede exceder los 100 caracteres.")
    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @NotNull(message = "La fecha de contratación es obligatoria.")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @PrePersist
    protected void onCreate() {
        if (this.hireDate == null) {
            this.hireDate = LocalDate.now();
        }
    }
}