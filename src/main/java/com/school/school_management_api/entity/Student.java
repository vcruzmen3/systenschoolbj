package com.school.school_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "El campo UserId es obligatorio.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private ApplicationUser user;

    @NotBlank(message = "El número de matrícula es obligatorio.")
    @Size(max = 50, message = "El número de matrícula no puede exceder los 50 caracteres.")
    @Column(name = "enrollment_number", nullable = false, length = 50, unique = true)
    private String enrollmentNumber;

    @NotBlank(message = "El grado o nivel académico es obligatorio.")
    @Size(max = 50, message = "El grado no puede exceder los 50 caracteres.")
    @Column(name = "grade_level", nullable = false, length = 50)
    private String gradeLevel;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Size(max = 150, message = "El nombre del tutor no puede exceder los 150 caracteres.")
    @Column(name = "guardian_name", length = 150)
    private String guardianName;

    @Size(max = 20, message = "El teléfono del tutor no puede exceder los 20 caracteres.")
    @Pattern(regexp = "^$|^[+]*[(]?[0-9]{1,4}[)]?[-\\s./0-9]*$", message = "El formato del teléfono del tutor no es válido.")
    @Column(name = "guardian_phone", length = 20)
    private String guardianPhone;

    @PrePersist
    protected void onCreate() {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = LocalDate.now();
        }
    }
}