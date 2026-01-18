**Endpoints:**

__AuthModule__<br>
login: POST http://localhost:8080/api/v1/auth/login<br>

__employeeModule__<br>
create employee: POST http://localhost:8080/api/v1/employees<br> 
Update employee: UPDATE http://localhost:8080/api/v1/employees/{employee_id}<br> 
Delete employee: DELETE http://localhost:8080/api/v1/employees/{employee_id}<br> 
Get employees(pagination): GET http://localhost:8080/api/v1/employees?page=0&size=10&sort=createdAt,desc<br>
Get employee by employee_id: GET http://localhost:8080/api/v1/employees/{employee_id}<br>  
create department: POST http://localhost:8080/api/v1/departments<br>
Update department: UPDATE http://localhost:8080/api/v1/departments/{department_id}<br>
Delete department: DELETE http://localhost:8080/api/v1/departments/{department_id}<br>
Get departments: GET http://localhost:8080/api/v1/departments<br>
Get department by department_id: GET http://localhost:8080/api/v1/departments/dep13<br>

__attendanceModule__<br>
check-in: POST http://localhost:8080/api/v1/attendance/check-in<br>
check-out: POST http://localhost:8080/api/v1/attendance/check-out<br>
attendance history query by employee_id: GET http://localhost:8080/api/v1/attendance/history?employeeId=emp001&startDate=2026-01-01&endDate=2026-01-13&page=0&size=10<br>

__tasksModule__<br>
create task: POST http://localhost:8080/api/v1/tasks<br> 
Reassign task: PATCH http://localhost:8080/api/v1/tasks/reassignment<br> 
status update: PATCH http://localhost:8080/api/v1/tasks/status<br> 
tasks by employee: GET http://localhost:8080/api/v1/tasks/employee/{employee_id}<br>
tasks by manager: GET http://localhost:8080/api/v1/tasks/manager/{manager}<br>

