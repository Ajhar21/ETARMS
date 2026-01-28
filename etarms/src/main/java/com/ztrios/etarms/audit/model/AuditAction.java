package com.ztrios.etarms.audit.model;

public final class AuditAction {

    private AuditAction() {
    }

    /* =========================
       AUTH MODULE
       ========================= */
    public static final String LOGIN = "LOGIN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LOGOUT_ALL = "LOGOUT_ALL";


    /* =========================
       EMPLOYEE MODULE
       ========================= */
    public static final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE";
    public static final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE";
    public static final String DELETE_EMPLOYEE = "DELETE_EMPLOYEE";
    public static final String UPLOAD_EMPLOYEE_PHOTO = "UPLOAD_EMPLOYEE_PHOTO";


    /* =========================
       DEPARTMENT MODULE
       ========================= */
    public static final String CREATE_DEPARTMENT = "CREATE_DEPARTMENT";
    public static final String UPDATE_DEPARTMENT = "UPDATE_DEPARTMENT";
    public static final String DELETE_DEPARTMENT = "DELETE_DEPARTMENT";


    /* =========================
       ATTENDANCE MODULE
       ========================= */
    public static final String CHECK_IN = "CHECK_IN";
    public static final String CHECK_OUT = "CHECK_OUT";


    /* =========================
       TASK MODULE
       ========================= */
    public static final String CREATE_TASK = "CREATE_TASK";
    public static final String REASSIGN_TASK = "REASSIGN_TASK";
    public static final String UPDATE_TASK_STATUS = "UPDATE_TASK_STATUS";


    /* =========================
       REPORTING MODULE
       ========================= */
    // Optional: only if want visibility on report usage
    public static final String GENERATE_ATTENDANCE_REPORT = "GENERATE_ATTENDANCE_REPORT";
    public static final String EXPORT_ATTENDANCE_REPORT = "EXPORT_ATTENDANCE_REPORT";
}
