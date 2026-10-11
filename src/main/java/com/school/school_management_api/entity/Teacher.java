package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "El campo UserId es obligatorio.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private ApplicationUser user;

    @NotBlank(message = "El código de empleado es obligatorio.")
    @Size(max = 50, message = "El código de empleado no puede exceder los 50 caracteres.")
    @Column(name = "employee_code", nullable = false, length = 50, unique = true)
    private String employeeCode;

    @NotBlank(message = "La especialidad es obligatoria.")
    @Size(max = 150, message = "La especialidad no puede exceder los 150 caracteres.")
    @Column(name = "speciality", nullable = false, length = 150)
    private String speciality;

    @Size(max = 100, message = "El grado académico no puede exceder los 100 caracteres.")
    @Column(name = "academic_degree", length = 100)
    private String academicDegree;

    @NotNull
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @PrePersist
    protected void onCreate() {
        if (this.hireDate == null) {
            this.hireDate = LocalDate.now();
        }
    }
}