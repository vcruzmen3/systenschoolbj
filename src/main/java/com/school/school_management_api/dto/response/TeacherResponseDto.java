package com.school.school_management_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDto {

    private UUID  id;          // ID numérico de Teacher
    private UUID userId;      // UUID de ApplicationUser
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String role;

    private String employeeCode;
    private String speciality;
    private String academicDegree;
    private LocalDateTime hireDate;
}