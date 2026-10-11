package com.school.school_management_api.repository;

import com.school.school_management_api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    // Buscar estudiante por su matrícula única
    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);

    // Buscar perfil de estudiante por el ID del usuario asociado (Relación 1:1)
    Optional<Student> findByUserId(UUID userId);

    // Verificar si ya existe una matrícula antes de registrar
    boolean existsByEnrollmentNumber(String enrollmentNumber);
}