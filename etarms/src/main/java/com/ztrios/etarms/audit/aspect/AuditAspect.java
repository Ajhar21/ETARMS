package com.ztrios.etarms.audit.aspect;

import com.ztrios.etarms.attendance.dto.AttendanceCheckInResponse;
import com.ztrios.etarms.attendance.dto.AttendanceCheckOutResponse;
import com.ztrios.etarms.attendance.dto.AttendanceRequest;
import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.employee.dto.DepartmentResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.identity.dto.AuthResponse;
import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.tasks.dto.TaskReassignResponse;
import com.ztrios.etarms.tasks.dto.TaskResponse;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
                "Employee deleted " + employeeId
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.uploadEmployeePhoto(..)) && args(employeeId, file)",
            returning = "photoUrl"
    )
    public void auditUploadEmployeePhoto(                           //must use all args in the method parameter
                                                                    String employeeId,
                                                                    MultipartFile file,
                                                                    String photoUrl
    ) {
        auditService.log(
                AuditAction.UPLOAD_EMPLOYEE_PHOTO,
                "Employee",
                employeeId,
                "Employee photo uploaded. URL: " + photoUrl
        );
    }


    /* =========================
       DEPARTMENT AUDIT
       ========================= */

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.create*(..))",
            returning = "response"
    )
    public void auditCreateDepartment(DepartmentResponse response) {
        auditService.log(
                AuditAction.CREATE_DEPARTMENT,
                "Department",
                response.getDepartmentId(),
                "Department created " + response.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.update*(..))",
            returning = "response"
    )
    public void auditUpdateDepartment(DepartmentResponse response) {
        auditService.log(
                AuditAction.UPDATE_DEPARTMENT,
                "Department",
                response.getDepartmentId(),
                "Department updated " + response.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.employee.service.*.delete*(..)) && args(departmentId)"
    )
    public void auditDeleteDepartment(String departmentId) {
        auditService.log(
                AuditAction.DELETE_DEPARTMENT,
                "Department",
                departmentId,
                "Department " + departmentId + " deleted"
        );
    }

    /* =========================
       ATTENDANCE AUDIT
       ========================= */

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.attendance.service.*.checkIn(..)) && args(request)",
            returning = "response"
    )
    public void auditCheckIn(AttendanceRequest request, AttendanceCheckInResponse response) {   //if args exists, must include args & returning both orderly
        auditService.log(
                AuditAction.CHECK_IN,
                "Attendance",
                response.employeeId(),
                "CheckIn employee: " + response.employeeId() + " checkIn time: " + response.checkInTime()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.attendance.service.*.checkOut(..))",
            returning = "response"
    )
    public void auditCheckOut(AttendanceCheckOutResponse response) {
        auditService.log(
                AuditAction.CHECK_OUT,
                "Attendance",
                response.employeeId(),
                "CheckOut employee: " + response.employeeId() + " checkOut time: " + response.checkOutTime()
        );
    }

    /* =========================
       TASK AUDIT
       ========================= */

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.createTask(..))",
            returning = "response"
    )
    public void auditCreateTask(TaskResponse response) {
        auditService.log(
                AuditAction.CREATE_TASK,
                "Task",
                response.taskId(),
                "Task created with title: " + response.title()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.reassignTask(..))",
            returning = "response"
    )
    public void auditReassignTask(TaskReassignResponse response) {
        auditService.log(
                AuditAction.REASSIGN_TASK,
                "Task",
                response.taskId(),
                "Task reassigned to: " + response.newAssignedTo()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.tasks.service.*.statusUpdate(..))",
            returning = "response"
    )
    public void auditUpdateTaskStatus(TaskResponse response) {
        auditService.log(
                AuditAction.UPDATE_TASK_STATUS,
                "Task",
                response.taskId(),
                "Task status: " + response.taskId() + " updated to: " + response.status()
        );
    }

    /* =========================
       AUTH AUDIT
       ========================= */

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.identity.service.*.login(..))",
            returning = "response"
    )
    public void auditLogin(AuthResponse response) {
        auditService.log(
                AuditAction.LOGIN,
                "User",
                response.username(),
                "User: " + response.username() + " login"
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.ztrios.etarms.identity.service.*.revokeToken(..)) && args(token, reason)"
    )
    public void auditLogout(RefreshToken token, String reason) {
        auditService.log(
                AuditAction.LOGOUT,
                "RefreshToken",
                token.getUser().getId().toString(),
                "User logout"
        );
    }

}
