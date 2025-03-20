package com.example.university_management;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadingFaculties {
    private static final String FILE_PATH = loginController.filePath;
    private static Faculties[] faculties;
    public static void loadFaculties() throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(3);
        int rowCount = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() > 0) { // Check if the row has actual cells
                rowCount++;
            }
        }
        faculties = new Faculties[rowCount];
        for (int i = 1; i <= rowCount; i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row == null) continue;
            faculties[i-1] = new Faculties(getCellValue(row.getCell(0)), getCellValue(row.getCell(1)), getCellValue(row.getCell(2)), getCellValue(row.getCell(3)),
                    getCellValue(row.getCell(4)), getCellValue(row.getCell(5)), getCellValue(row.getCell(6)), getCellValue(row.getCell(7)));
        }
        wb.close();  // Close workbook
        fins.close();
    }
    static String getCellValue(Cell cell) {
        if (cell == null) return ""; // Return empty string for null cells

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim(); // Return string value
            case NUMERIC -> String.valueOf(cell.getNumericCellValue()).trim(); // Convert numeric value to string
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim(); // Convert boolean to string
            default -> ""; // Return empty string for other cell types
        };
    }
    public static boolean verifyLogin(String enteredID, String enteredPass) throws IOException {
        loadFaculties(); // Load student data
        if (enteredID.isEmpty() || enteredPass.isEmpty()) return false; // Reject empty credentials

        // Loop through students to check for a matching ID and password
        for (Faculties faculties1 : faculties) {
            if(faculties1==null) continue;
            if (enteredID.equals(faculties1.ID) && enteredPass.equals(faculties1.password)) {
                return true; // Login successful
            }
        }
        return false; // Login failed
    }
    public static Faculties facultiesInfo(String userID) throws IOException{
        for (Faculties faculties1: faculties){
            if (userID.equals(faculties1.ID)){
                return new Faculties(faculties1.ID, faculties1.name, faculties1.degree, faculties1.researchInterest, faculties1.email, faculties1.officeLocation,
                        faculties1.coursesOffered, faculties1.password);
            }
        }
        return null;
    }
    public static Faculties[] getAllFaculty(){
        return faculties;
    }
}
