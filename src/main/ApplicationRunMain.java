package main;

import service.EmployeeService;

public class ApplicationRunMain {

    public static void main(String[] args) {

        EmployeeService employeeService = new EmployeeService();
        employeeService.addEmployee();

    }
}
