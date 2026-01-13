package com.ztrios.etarms.employee.repository;

import com.ztrios.etarms.employee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    boolean existsByName(String name);

    boolean existsByDepartmentId(String departmentId);

    Optional<Department> findByName(String name);      // find by name

    Optional<Department> findByDepartmentId(String departmentId); // optional if using public ID

    void deleteByDepartmentId(String departmentId);
}
