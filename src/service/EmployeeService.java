package service;

import enums.Role;
import model.Employee;
import repository.EmployeeRepository;

import java.util.Scanner;

public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final Scanner scanner;

    public EmployeeService() {
        this.employeeRepo = new EmployeeRepository();
        this.scanner = new Scanner(System.in);
    }

    public void addEmployee() {
        try {
            System.out.println("=== Add New Employee ===");

            System.out.print("Enter employee name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter employee email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter department: ");
            String department = scanner.nextLine().trim();

            Role role = readRoleFromConsole();

            Employee employee = new Employee(
                    name,
                    email,
                    role,
                    department
            );

            boolean created = employeeRepo.createEmployee(employee);

            if (created) {
                System.out.println("Employee created successfully.");
            } else {
                System.out.println("Failed to create employee.");
            }

        } catch (Exception e) {
            System.err.println("Error while adding employee: " + e.getMessage());
        }
    }

    private Role readRoleFromConsole() {
        while (true) {
            System.out.print("Enter role (EMPLOYEE / MANAGER): ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                return Role.valueOf(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid role. Please enter EMPLOYEE or MANAGER.");
            }
        }
    }
}
