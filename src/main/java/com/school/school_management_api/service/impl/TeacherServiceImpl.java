package com.school.school_management_api.service.impl;

import com.school.school_management_api.dto.request.CreateTeacherDto;
import com.school.school_management_api.dto.request.UpdateTeacherDto;
import com.school.school_management_api.dto.response.TeacherResponseDto;
import com.school.school_management_api.entity.ApplicationUser;
import com.school.school_management_api.entity.Role;
import com.school.school_management_api.entity.Teacher;
import com.school.school_management_api.entity.UserRole;
import com.school.school_management_api.exception.BadRequestException;
import com.school.school_management_api.exception.ResourceNotFoundException;
import com.school.school_management_api.repository.RoleRepository;
import com.school.school_management_api.repository.TeacherRepository;
import com.school.school_management_api.repository.UserRepository;
import com.school.school_management_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherRepository.findAllWithUser().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponseDto getTeacherById(UUID id) { // Cambiado a UUID
        Teacher teacher = teacherRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún profesor con el ID " + id));
        return mapToDto(teacher);
    }





        @Override
        @Transactional // Garantiza que si falla la creación del Teacher, se revierta (rollback) la creación del usuario
        public TeacherResponseDto createTeacher(CreateTeacherDto createDto) {

            // 1. Validaciones previas utilizando sintaxis de Record
            if (userRepository.existsByEmail(createDto.email())) {
                throw new BadRequestException("El correo electrónico ya se encuentra registrado.");
            }

            if (teacherRepository.existsByEmployeeCode(createDto.employeeCode())) {
                throw new BadRequestException("El código de empleado ya se encuentra registrado.");
            }

            Role teacherRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException("El rol 'ADMIN' --s>> no se encuentra configurado en el sistema."));

            // 2. Instanciar y configurar Usuario
            ApplicationUser user = new ApplicationUser();
            user.setEmail(createDto.email());
            user.setUsername(createDto.email()); // O si manejas username independiente, usa createDto.username()
            user.setFirstName(createDto.firstName());
            user.setLastName(createDto.lastName());
            user.setPassword(passwordEncoder.encode(createDto.password()));
            user.setIsActive(true);

            // Vincular el Rol ANTES de guardar para persistir en una sola transacción
            UserRole userRole = new UserRole(user, teacherRole);
            user.addUserRole(userRole);

            ApplicationUser savedUser = userRepository.save(user);

            // 3. Crear y asociar el Perfil de Profesor
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setEmployeeCode(createDto.employeeCode());
            teacher.setSpeciality(createDto.speciality());
            teacher.setAcademicDegree(createDto.academicDegree());
            teacher.setHireDate(LocalDate.now());

            Teacher savedTeacher = teacherRepository.save(teacher);

            return mapToDto(savedTeacher);
        }


    @Override
    @Transactional
    public TeacherResponseDto updateTeacher(UUID id, UpdateTeacherDto updateDto) {
        log.info("===> INICIO DE ACTUALIZACIÓN: Buscando profesor con ID: {}", id);

        // 1. Búsqueda del registro en la base de datos
        Teacher teacher = teacherRepository.findByIdWithUser(id)
                .orElseThrow(() -> {
                    log.error("====> ERROR ACTUALIZACIÓN: Profesor no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("No se encontró ningún profesor con el ID " + id);
                });

        log.debug("Profesor encontrado en BD. Usuario asignado: {} {}, Código actual: {}",
                teacher.getUser() != null ? teacher.getUser().getFirstName() : "N/A",
                teacher.getUser() != null ? teacher.getUser().getLastName() : "N/A",
                teacher.getEmployeeCode());

        // 2. Validación de duplicidad de código de empleado excluyendo el registro actual
        if (teacherRepository.existsByEmployeeCodeAndIdNot(updateDto.employeeCode(), id)) {
            log.warn("====> ADVERTENCIA ACTUALIZACIÓN: El código de empleado '{}' ya está en uso por otro registro.", updateDto.employeeCode());
            throw new BadRequestException("El código de empleado '" + updateDto.employeeCode() + "' ya está registrado por otro profesor.");
        }

        // 3. Actualización de los datos del Usuario relacionado
        if (teacher.getUser() != null) {
            log.info("Actualizando datos personales del usuario (ID: {})", teacher.getUser().getId());
            teacher.getUser().setFirstName(updateDto.firstName());
            teacher.getUser().setLastName(updateDto.lastName());

            if (updateDto.isActive() != null) {
                teacher.getUser().setIsActive(updateDto.isActive());
            }
        } else {
            log.warn("El profesor con ID {} no tiene un ApplicationUser vinculado en la BD.", id);
        }

        // 4. Actualización de los datos específicos de Profesor
        log.info("Actualizando campos específicos de la entidad Teacher para ID: {}", id);
        teacher.setEmployeeCode(updateDto.employeeCode());
        teacher.setSpeciality(updateDto.speciality());
        teacher.setAcademicDegree(updateDto.academicDegree());

        if (updateDto.hireDate() != null) {
            teacher.setHireDate(updateDto.hireDate()); // Asignación directa de LocalDate
        }

        // 5. Persistencia en la base de datos
        Teacher updatedTeacher = teacherRepository.save(teacher);
        log.info("===> ACTUALIZACIÓN EXITOSA: Profesor con ID {} actualizado correctamente.", updatedTeacher.getId());

        return mapToDto(updatedTeacher);
    }
    @Override
    @Transactional
    public void deleteTeacher(UUID id) { // Cambiado a UUID
        Teacher teacher = teacherRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún profesor con el ID " + id));

        ApplicationUser user = teacher.getUser();
        teacherRepository.delete(teacher);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    private TeacherResponseDto mapToDto(Teacher teacher) {
        TeacherResponseDto dto = new TeacherResponseDto();
        dto.setId(teacher.getId()); // Ambos campos son UUID

        if (teacher.getUser() != null) {
            ApplicationUser user = teacher.getUser();
            dto.setUserId(user.getId()); // Ambos campos son UUID
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setIsActive(user.getIsActive());

            if (user.getUserRoles() != null) {
                String roles = user.getUserRoles().stream()
                        .filter(ur -> ur.getRole() != null)
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.joining(", "));
                dto.setRole(roles);
            }
        }

        dto.setEmployeeCode(teacher.getEmployeeCode());
        dto.setSpeciality(teacher.getSpeciality());
        dto.setAcademicDegree(teacher.getAcademicDegree());

        if (teacher.getHireDate() != null) {
            dto.setHireDate(teacher.getHireDate().atStartOfDay());
        }

        return dto;
    }
}