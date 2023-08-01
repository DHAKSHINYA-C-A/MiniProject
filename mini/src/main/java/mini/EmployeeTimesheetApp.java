package mini;

import java.sql.*;
import java.util.Scanner;

interface EmployeeManagement {
    void viewTotalSalary(Connection conn, int empId) throws SQLException;
    void applyForLeave(Connection conn, int empId, Scanner scanner) throws SQLException;
    void enterWorkHours(Connection conn, int empId, Scanner scanner) throws SQLException;
}

abstract class Employee {
    private String empName;
    private String empRole;

    public Employee(String empName, String empRole) {
        this.empName = empName;
        this.empRole = empRole;
    }

    public abstract int addEmployee(Connection conn) throws SQLException;

    public String getEmpName() {
        return empName;
    }

    public String getEmpRole() {
        return empRole;
    }
}

class RegularEmployee extends Employee implements EmployeeManagement {

    public RegularEmployee(String empName, String empRole) {
        super(empName, empRole);
    }
    public void viewWorkHoursApprovalStatus(Connection conn, int empId) throws SQLException {
        String selectWorkHoursApprovalSQL = "SELECT work_date, hours_worked, approved " +
                "FROM timesheets WHERE emp_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(selectWorkHoursApprovalSQL)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String workDate = rs.getString("work_date");
                double hoursWorked = rs.getDouble("hours_worked");
                String status = rs.getString("approved");
                System.out.println("Work Date: " + workDate + ", Hours Worked: " + hoursWorked + ", Status: " + status);
            }
        }
    }
    public void viewLeaveApprovalStatus(Connection conn, int empId) throws SQLException {
        String selectLeaveApprovalSQL = "SELECT leave_request_id, start_date, end_date, reason, approved " +
                "FROM leave_requests WHERE emp_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(selectLeaveApprovalSQL)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int leaveRequestId = rs.getInt("leave_request_id");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String reason = rs.getString("reason");
                String leaveStatus = rs.getString("approved");
                System.out.println("Leave Request ID: " + leaveRequestId + ", Start Date: " + startDate +
                        ", End Date: " + endDate + ", Reason: " + reason + ", Status: " + leaveStatus);
            }
        }
    }

    @Override
    public int addEmployee(Connection conn) throws SQLException {
        String selectEmployeeSQL = "SELECT emp_id FROM employees WHERE emp_name = ? AND emp_role = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(selectEmployeeSQL)) {
            pstmt.setString(1, getEmpName());
            pstmt.setString(2, getEmpRole());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
  
                int existingEmpId = rs.getInt("emp_id");
                System.out.println("Employee already exists with ID: " + existingEmpId);
                return existingEmpId;
            } else {
        
                String insertEmployeeSQL = "INSERT INTO employees (emp_name, emp_role) VALUES (?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertEmployeeSQL, Statement.RETURN_GENERATED_KEYS)) {
                    insertPstmt.setString(1, getEmpName());
                    insertPstmt.setString(2, getEmpRole());
                    insertPstmt.executeUpdate();

                    ResultSet generatedKeys = insertPstmt.getGeneratedKeys();
                    int empId = -1;
                    if (generatedKeys.next()) {
                        empId = generatedKeys.getInt(1);
                        System.out.println("Employee added with ID: " + empId);
                    }

                    return empId;
                }
            }
        }
    }

    @Override
    public void viewTotalSalary(Connection conn, int empId) throws SQLException {
        String selectTotalSalarySQL = "SELECT SUM(hours_worked) AS total_hours, SUM(hours_worked*10000) AS total_salary " +
                "FROM timesheets JOIN employees ON timesheets.emp_id = employees.emp_id " +
                "WHERE timesheets.emp_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(selectTotalSalarySQL)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double totalHours = rs.getDouble("total_hours");
                double totalSalary = rs.getDouble("total_salary");
                System.out.println("Total Hours Worked: " + totalHours);
                System.out.println("Total Salary: " + totalSalary);
            } else {
                System.out.println("No timesheets found for the employee.");
            }
        }
    }

    @Override
    public void applyForLeave(Connection conn, int empId, Scanner scanner) throws SQLException {
        System.out.print("Enter leave start date (YYYY-MM-DD): ");
        String leaveStartDate = scanner.nextLine();
        System.out.print("Enter leave end date (YYYY-MM-DD): ");
        String leaveEndDate = scanner.nextLine();
        System.out.print("Enter reason for leave: ");
        String leaveReason = scanner.nextLine();

        String insertLeaveRequestSQL = "INSERT INTO leave_requests (emp_id, start_date, end_date, reason) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertLeaveRequestSQL)) {
            pstmt.setInt(1, empId);
            pstmt.setString(2, leaveStartDate);
            pstmt.setString(3, leaveEndDate);
            pstmt.setString(4, leaveReason);
            pstmt.executeUpdate();
            System.out.println("Leave request submitted successfully!");
        }
    }

    @Override
    public void enterWorkHours(Connection conn, int empId, Scanner scanner) throws SQLException {
        System.out.print("Enter the work date (YYYY-MM-DD): ");
        String workDateStr = scanner.nextLine();
        System.out.print("Enter hours worked: ");
        double hoursWorked = scanner.nextDouble();

        String insertTimesheetSQL = "INSERT INTO timesheets (emp_id, work_date, hours_worked) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertTimesheetSQL)) {
            pstmt.setInt(1, empId);
            pstmt.setString(2, workDateStr);
            pstmt.setDouble(3, hoursWorked);
            pstmt.executeUpdate();
            System.out.println("Work hours entered successfully!");
        }
    }
}

public class EmployeeTimesheetApp {
    private static final String DB_URL = "jdbc:mysql://localhost/timesheet_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "c@df@mily1A";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Enter your name: ");
            String empName = scanner.nextLine();
            System.out.print("Enter your role: ");
            String empRole = scanner.nextLine();

            RegularEmployee employee = new RegularEmployee(empName, empRole);

            int empId = employee.addEmployee(conn);
            if (empId != -1) {
                while (true) {
                    System.out.println("1. Enter Work Hours");
                    System.out.println("2. Request for Leave");
                    System.out.println("3. Check your total salary");
                    System.out.println("4. View Work Hours Approval Status"); // New option
                    System.out.println("5. View Leave Approval Status"); // New option
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer

                    switch (choice) {
                        case 1:
                            employee.enterWorkHours(conn, empId, scanner);
                            break;
                        case 2:
                            employee.applyForLeave(conn, empId, scanner);
                            break;
                        case 3:
                            employee.viewTotalSalary(conn, empId);
                            break;
                        case 4:
                            employee.viewWorkHoursApprovalStatus(conn, empId); // New option
                            break;
                        case 5:
                            employee.viewLeaveApprovalStatus(conn, empId); // New option
                            break;
                        case 6:
                            System.out.println("Goodbye!");
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
