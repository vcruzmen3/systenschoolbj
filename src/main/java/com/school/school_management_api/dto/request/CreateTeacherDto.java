package com.school.school_management_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record CreateTeacherDto(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio.")
        @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres.")
        String lastName,

        @NotBlank(message = "El correo electrónico es obligatorio.")
        @Email(message = "El formato del correo electrónico no es válido.")
        @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password,

        @NotBlank(message = "El código de empleado es obligatorio.")
        @Size(max = 20, message = "El código de empleado no puede exceder los 20 caracteres.")
        String employeeCode,

        @Size(max = 100, message = "La especialidad no puede exceder los 100 caracteres.")
        String speciality,

        @Size(max = 100, message = "El grado académico no puede exceder los 100 caracteres.")
        String academicDegree
) {
    // Compact constructor para asignar valores por defecto a campos opcionales si vienen nulos
    public CreateTeacherDto {
        if (speciality == null) speciality = "";
        if (academicDegree == null) academicDegree = "";
    }
}