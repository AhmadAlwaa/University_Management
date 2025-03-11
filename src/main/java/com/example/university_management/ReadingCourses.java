package com.example.university_management;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadingCourses {
    private static final String FILE_PATH = loginController.filePath;
    private static Course[] courses;

    public static void loadCourses() throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(1);
        int rowCount = sheet.getLastRowNum();
        courses = new Course[rowCount];

        for (int i = 1; i <= rowCount; i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row == null) continue;

            courses[i - 1] = new Course(
                    getCellValue(row.getCell(0)),
                    getCellValue(row.getCell(1)),
                    getCellValue(row.getCell(2)),
                    getCellValue(row.getCell(3)),
                    getCellValue(row.getCell(4)),
                    getCellValue(row.getCell(5)),
                    getCellValue(row.getCell(6)), // Final Exam Date
                    getCellValue(row.getCell(7)),
                    getCellValue(row.getCell(8))
            );
        }

        wb.close();  // Close workbook
        fins.close();
    }

    static String getCellValue(Cell cell) {
        if (cell == null) return ""; // Return empty string for null cells

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim(); // Return string value
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    return dateFormat.format(date); // Format date correctly
                }
                return String.valueOf(cell.getNumericCellValue()).trim(); // Convert numeric value to string
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim(); // Convert boolean to string
            default:
                return ""; // Return empty string for other cell types
        }
    }

    public static Course[] getAllCourseInfo() {
        return courses;
    }
}