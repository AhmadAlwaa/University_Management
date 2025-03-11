package com.example.university_management;
// Package declaration for the University Management System

import org.apache.poi.ss.usermodel.*;
// Import Apache POI classes for reading Excel files

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Reading_Student {
    // File path to the Excel sheet containing student data
    private static final String FILE_PATH = loginController.filePath;
    private static Student[] students; // Array to store student records

    // Method to load student credentials from the Excel file
    public static void loadStudentCredentials() throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH)); // Open file input stream
        Workbook wb = WorkbookFactory.create(fins); // Create workbook from the file
        Sheet sheet = wb.getSheetAt(2); // Access the third sheet (index 2)

        int rowCount = sheet.getLastRowNum(); // Get the total number of students (excluding header)
        students = new Student[rowCount]; // Initialize student array

        // Loop through each row (starting from row 1, skipping header)
        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue; // Skip empty rows

            // Extract student ID and password from specific columns
            Cell idCell = row.getCell(0);  // Column A (Student ID)
            Cell passCell = row.getCell(11); // Column L (Password)

            // Retrieve values from cells, handling different data types
            String studentID = getCellValue(idCell);
            String password = getCellValue(passCell);

            // Create a new Student object and populate it with data from the row
            students[i - 1] = new Student(
                    studentID,
                    password,
                    getCellValue(row.getCell(1)),  // Name (Column B)
                    getCellValue(row.getCell(2)),  // Address (Column C)
                    getCellValue(row.getCell(3)),  // Telephone (Column D)
                    getCellValue(row.getCell(4)),  // Email (Column E)
                    getCellValue(row.getCell(5)),  // Academic Level (Column F)
                    getCellValue(row.getCell(6)),  // Current Semester (Column G)
                    getCellValue(row.getCell(7)),  // Profile Photo (Column H)
                    getCellValue(row.getCell(8)),  // Subject Rejection (Column I)
                    getCellValue(row.getCell(9)),  // Thesis (Column J)
                    getCellValue(row.getCell(10))  // Progress (Column K)
            );
        }

        wb.close();  // Close workbook
        fins.close();  // Close file input stream
    }

    // Helper method to retrieve cell values, handling different cell types
    static String getCellValue(Cell cell) {
        if (cell == null) return ""; // Return empty string for null cells

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim(); // Return string value
            case NUMERIC -> String.valueOf(cell.getNumericCellValue()).trim(); // Convert numeric value to string
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim(); // Convert boolean to string
            default -> ""; // Return empty string for other cell types
        };
    }

    // Method to verify student login credentials
    public static boolean verifyLogin(String enteredID, String enteredPass) throws IOException {
        loadStudentCredentials(); // Load student data
        if (enteredID.isEmpty() || enteredPass.isEmpty()) return false; // Reject empty credentials

        // Loop through students to check for a matching ID and password
        for (Student student : students) {
            if (enteredID.equals(student.studentID) && enteredPass.equals(student.password)) {
                return true; // Login successful
            }
        }
        return false; // Login failed
    }

    // Method to retrieve a student's information based on their user ID
    public static Student studentInfo(String userID) throws IOException {
        for (Student student : students) {
            if (userID.equals(student.studentID)) {
                // Return a new Student object with the found student's details
                return new Student(student.studentID, student.password, student.name, student.address,
                        student.telephone, student.email, student.academicLevel, student.currSem,
                        student.profilePhoto, student.subjRej, student.thesis, student.progress);
            }
        }
        return null; // Return null if student is not found
    }

    // Method to return all loaded students
    public static Student[] getAllStudents() {
        return students; // Return the student array
    }
}
