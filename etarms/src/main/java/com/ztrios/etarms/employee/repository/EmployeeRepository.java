package com.ztrios.etarms.employee.repository;

import com.ztrios.etarms.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findByEmployeeId(String employeeId); // optional if using public ID

    void deleteByEmployeeId(String employeeId);

    //    boolean isActive(String employeeId);
    @Query("SELECT COUNT(e) > 0 FROM Employee e " +
            "WHERE e.employeeId = :employeeId AND e.employmentStatus = com.ztrios.etarms.employee.enums.EmploymentStatus.ACTIVE")
    boolean isActive(@Param("employeeId") String employeeId);
}
