package com.ztrios.etarms.employee.repository;

import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findByEmployeeId(String employeeId); // optional if using public ID

    void deleteByEmployeeId(String employeeId);
}
