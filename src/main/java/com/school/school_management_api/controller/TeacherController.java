package com.school.school_management_api.controller;

import com.school.school_management_api.dto.request.CreateTeacherDto;
import com.school.school_management_api.dto.request.UpdateTeacherDto;
import com.school.school_management_api.dto.response.TeacherResponseDto;
import com.school.school_management_api.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/teachers") // Context path /api/v1 ya viene definido en application.yml
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // GET: /api/v1/teachers
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'USER')")
    public ResponseEntity<List<TeacherResponseDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    // GET: /api/v1/teachers/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'USER')")
    public ResponseEntity<TeacherResponseDto> getTeacherById(@PathVariable UUID id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    // POST: /api/v1/teachers
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTeacher(@Valid @RequestBody CreateTeacherDto createDto) {
        try {
            log.info("===> Intentando crear un nuevo profesor con código: {}", createDto.employeeCode());

            TeacherResponseDto createdTeacher = teacherService.createTeacher(createDto);

            // Ajustado a getId() o id() dependiendo de si TeacherResponseDto es Clase o Record
            log.info("===> Profesor creado exitosamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);

        } catch (Exception ex) {
            log.error("====> ERROR CRÍTICO AL CREAR PROFESOR: ", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", ex.getMessage() != null ? ex.getMessage() : "Error sin mensaje",
                            "cause", ex.getCause() != null ? ex.getCause().getMessage() : "Sin causa raíz directa"
                    ));
        }
    }

    // PUT: /api/v1/teachers/{id}
// PUT: /api/v1/teachers/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTeacherDto updateDto) {
        try {
            log.info("===> PETICIÓN PUT /teachers/{}: Iniciando actualización para el profesor con ID {}", id, id);

            TeacherResponseDto updatedTeacher = teacherService.updateTeacher(id, updateDto);

            log.info("===> PETICIÓN PUT /teachers/{}: Profesor actualizado con éxito.", id);
            return ResponseEntity.ok(updatedTeacher);

        } catch (Exception ex) {
            log.error("====> ERROR AL ACTUALIZAR PROFESOR CON ID {}: {}", id, ex.getMessage(), ex);
            throw ex; // Re-lanza la excepción para que la capture tu GlobalExceptionHandler
        }
    }

    // DELETE: /api/v1/teachers/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable UUID id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}