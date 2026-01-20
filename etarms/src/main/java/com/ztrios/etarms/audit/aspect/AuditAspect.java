package com.ztrios.etarms.audit.aspect;

import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.entity.Employee;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    /* =========================
       EMPLOYEE AUDIT
       ========================= */

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.create*(..))",
            returning = "response"
    )
    public void auditCreateEmployee(EmployeeResponse response) {
        auditService.log(
                AuditAction.CREATE_EMPLOYEE,
                "Employee",
                response.employeeId(),
                "Employee created " + response.firstName() + " " + response.lastName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.update*(..))",
            returning = "response"
    )
    public void auditUpdateEmployee(EmployeeResponse response) {
        auditService.log(
                AuditAction.UPDATE_EMPLOYEE,
                "Employee",
                response.employeeId(),
                "Employee updated " + response.firstName() + " " + response.lastName()
        );
    }


    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.delete(..)) && args(employeeId)"
    )
    public void auditDeleteEmployee(String employeeId) {
        auditService.log(
                AuditAction.DELETE_EMPLOYEE,
                "Employee",
                employeeId,
                "Employee deleted " +employeeId
        );
    }

//    /* =========================
//       DEPARTMENT AUDIT
//       ========================= */
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.department.service.*.create*(..)) && args(department,..)"
//    )
//    public void auditCreateDepartment(Department department) {
//        AuditLogger.createDepartment(auditService, department);
//    }
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.department.service.*.update*(..)) && args(department,..)"
//    )
//    public void auditUpdateDepartment(Department department) {
//        AuditLogger.updateDepartment(auditService, department);
//    }
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.department.service.*.delete*(..)) && args(department,..)"
//    )
//    public void auditDeleteDepartment(Department department) {
//        AuditLogger.deleteDepartment(auditService, department);
//    }
//
//    /* =========================
//       ATTENDANCE AUDIT
//       ========================= */
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.attendance.service.*.checkIn(..)) && args(attendance,..)"
//    )
//    public void auditCheckIn(Attendance attendance) {
//        AuditLogger.checkIn(auditService, attendance);
//    }
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.attendance.service.*.checkOut(..)) && args(attendance,..)"
//    )
//    public void auditCheckOut(Attendance attendance) {
//        AuditLogger.checkOut(auditService, attendance);
//    }
//
//    /* =========================
//       TASK AUDIT
//       ========================= */
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.create*(..)) && args(task,..)"
//    )
//    public void auditCreateTask(Task task) {
//        AuditLogger.createTask(auditService, task);
//    }
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.reassign*(..)) && args(task,oldAssignee,newAssignee,..)"
//    )
//    public void auditReassignTask(Task task, String oldAssignee, String newAssignee) {
//        AuditLogger.reassignTask(auditService, task, oldAssignee, newAssignee);
//    }
//
//    @AfterReturning(
//            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.update*(..)) && args(task,..)"
//    )
//    public void auditUpdateTaskStatus(Task task) {
//        AuditLogger.updateTaskStatus(auditService, task);
//    }
}
