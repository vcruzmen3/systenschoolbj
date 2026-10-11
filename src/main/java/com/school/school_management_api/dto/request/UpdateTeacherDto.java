package com.school.school_management_api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTeacherDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
        String lastName,

        @NotNull(message = "El estado del usuario (activo/inactivo) es obligatorio")
        Boolean isActive,

        @NotBlank(message = "El código de empleado es obligatorio")
        @Size(max = 20, message = "El código de empleado no puede exceder los 20 caracteres")
        String employeeCode,

        @NotBlank(message = "La especialidad es obligatoria")
        @Size(max = 100, message = "La especialidad no puede exceder los 100 caracteres")
        String speciality,

        @NotBlank(message = "El grado académico es obligatorio")
        @Size(max = 50, message = "El grado académico no puede exceder los 50 caracteres")
        String academicDegree,

        @NotNull(message = "La fecha de contratación es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate hireDate
) {}