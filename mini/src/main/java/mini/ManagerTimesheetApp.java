package mini;

import java.sql.*;
import java.util.Scanner;

public class ManagerTimesheetApp {
    private static final String DB_URL = "jdbc:mysql://localhost/timesheet_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "c@df@mily1A";

    private enum ApprovalStatus {
        APPROVED,
        PENDING,
        REJECTED
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("1. Review Timesheets");
                System.out.println("2. Approve Timesheets");
                System.out.println("3. Review LeaveRequests");
                System.out.println("4. Approve LeaveRequests");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1:
                        reviewTimesheets(conn, scanner);
                        break;
                    case 2:
                        approveTimesheets(conn, scanner);
                        break;
                    case 3:
                        reviewLeaveRequests(conn, scanner);
                        break;
                    case 4:
                        approveLeaveRequests(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void approveLeaveRequests(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter leave request ID to approve: ");
        int leaveRequestId = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter your choice: ");
        int approvalChoice = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        String updateApprovedSQL = "UPDATE leave_requests SET approved = ? WHERE leave_request_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateApprovedSQL)) {
            if (approvalChoice == 1) {
                pstmt.setString(1, ApprovalStatus.APPROVED.name()); // Set the approval status to "APPROVED"
            } else if (approvalChoice == 2) {
                pstmt.setString(1, ApprovalStatus.REJECTED.name()); // Set the approval status to "REJECTED"
            } else {
                System.out.println("Invalid choice. Leave request not updated.");
                return;
            }

            pstmt.setInt(2, leaveRequestId);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Leave request updated successfully.");
            } else {
                System.out.println("Invalid leave request ID.");
            }
        }
    }

    private static void reviewLeaveRequests(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        String selectLeaveRequestsSQL = "SELECT * FROM leave_requests WHERE emp_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectLeaveRequestsSQL)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int leaveRequestId = rs.getInt("leave_request_id");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String reason = rs.getString("reason");
                String approvalStatusStr = rs.getString("approved"); // Get the approval status as a string from the database
                ApprovalStatus approvalStatus = ApprovalStatus.valueOf(approvalStatusStr); // Convert the string to enum

                System.out.println("Leave Request ID: " + leaveRequestId);
                System.out.println("Start Date: " + startDate);
                System.out.println("End Date: " + endDate);
                System.out.println("Reason: " + reason);
                System.out.println("Approval Status: " + approvalStatus.name()); // Display the approval status as a string
                System.out.println("------------------------------");
            }
        }
    }

    private static void reviewTimesheets(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        String selectTimesheetsSQL = "SELECT * FROM timesheets WHERE emp_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectTimesheetsSQL)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int timesheetId = rs.getInt("timesheet_id");
                String workDate = rs.getString("work_date");
                double hoursWorked = rs.getDouble("hours_worked");
                String approvalStatusStr = rs.getString("approved"); // Get the approval status as a string from the database
                ApprovalStatus approvalStatus = ApprovalStatus.valueOf(approvalStatusStr); // Convert the string to enum

                System.out.println("Timesheet ID: " + timesheetId);
                System.out.println("Work Date: " + workDate);
                System.out.println("Hours Worked: " + hoursWorked);
                System.out.println("Approval Status: " + approvalStatus.name()); // Display the approval status as a string
                System.out.println("------------------------------");
            }
        }
    }

    private static void approveTimesheets(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter your choice: ");
        int approvalChoice = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        String updateApprovedSQL = "UPDATE timesheets SET approved = ? WHERE emp_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateApprovedSQL)) {
            if (approvalChoice == 1) {
                pstmt.setString(1, ApprovalStatus.APPROVED.name()); // Set the approval status to "APPROVED"
            } else if (approvalChoice == 2) {
                pstmt.setString(1, ApprovalStatus.REJECTED.name()); // Set the approval status to "REJECTED"
            } else {
                System.out.println("Invalid choice. Timesheets not updated.");
                return;
            }

            pstmt.setInt(2, empId);
            int rowsUpdated = pstmt.executeUpdate();

            System.out.println(rowsUpdated + " timesheets updated.");
        }
    }
}
