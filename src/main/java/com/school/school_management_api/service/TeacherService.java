package com.school.school_management_api.service;

import com.school.school_management_api.dto.request.CreateTeacherDto;
import com.school.school_management_api.dto.request.UpdateTeacherDto;
import com.school.school_management_api.dto.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

public interface TeacherService {

    /**
     * Obtiene la lista de todos los profesores registrados.
     *
     * @return Lista de DTOs de profesores.
     */
    List<TeacherResponseDto> getAllTeachers();

    /**
     * Busca un profesor por su identificador único (ID).
     *
     * @param id Identificador único (UUID) del profesor.
     * @return DTO con la información del profesor.
     */
    TeacherResponseDto getTeacherById(UUID id);

    /**
     * Crea un nuevo profesor junto con su cuenta de usuario base en una operación transaccional.
     *
     * @param createDto DTO con la información de registro.
     * @return DTO del profesor creado.
     */
    TeacherResponseDto createTeacher(CreateTeacherDto createDto);

    /**
     * Actualiza la información del perfil del profesor y su usuario asociado.
     *
     * @param id Identificador único (UUID) del profesor a actualizar.
     * @param updateDto DTO con los datos modificados.
     * @return DTO con la información actualizada.
     */
    TeacherResponseDto updateTeacher(UUID id, UpdateTeacherDto updateDto);

    /**
     * Elimina el registro del profesor y su usuario asociado de la base de datos.
     *
     * @param id Identificador único (UUID) del profesor a eliminar.
     */
    void deleteTeacher(UUID id);
}