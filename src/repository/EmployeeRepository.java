package repository;

import model.Employee;
import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeRepository {

    private static final String INSERT_EMPLOYEE_SQL =
            """
            INSERT INTO employees (name, email, role, department)
            VALUES (?, ?, ?::employee_role, ?)
            """;

    public boolean createEmployee(Employee employee) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_EMPLOYEE_SQL)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getRole().name());
            ps.setString(4, employee.getDepartment());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted == 1;

        } catch (SQLException e) {
            handleSqlException(e);
            return false;
        }
    }

    private void handleSqlException(SQLException e) {

        // Unique constraint violation (email)
        if ("23505".equals(e.getSQLState())) {
            System.err.println("Employee with this email already exists.");
        } else {
            System.err.println("Database error while creating employee.");
            e.printStackTrace();
        }
    }
}

