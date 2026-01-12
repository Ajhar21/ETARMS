package com.ztrios.etarms.employee.repository;

import com.ztrios.etarms.employee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    boolean existsByName(String name);

    @Query("SELECT d.departmentId FROM Department d ORDER BY d.departmentId DESC")
    String findLastDepartmentId();
}
