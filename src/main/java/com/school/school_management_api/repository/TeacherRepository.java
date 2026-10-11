package com.school.school_management_api.repository;

import com.school.school_management_api.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, UUID id);

    // Carga ansiosa para evitar N+1 trayendo usuario y sus roles
    @Query("SELECT t FROM Teacher t JOIN FETCH t.user u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role")
    List<Teacher> findAllWithUser();

    @Query("SELECT t FROM Teacher t JOIN FETCH t.user u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE t.id = :id")
    Optional<Teacher> findByIdWithUser(@Param("id") UUID id);
}