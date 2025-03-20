package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.example.university_management.Reading_Student.getCellValue;

public class ReadingSubject {
    private static final String FILE_PATH = loginController.filePath;
    private static Subject[] subjects;
    public static void loadSubjects() throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(0);
        int rowCount = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() > 0) { // Check if the row has actual cells
                rowCount++;
            }
        }
        subjects = new Subject[rowCount];
        for (int i = 1; i <= rowCount; i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell cellCode = row.getCell(0);
            Cell cellName = row.getCell(1);
            if ((cellCode == null || cellCode.getCellType() == CellType.BLANK) &&
                    (cellName == null || cellName.getCellType() == CellType.BLANK)) {
                break; // Stop reading further if both cells are empty
            }
            String subjCode = getCellValue(cellCode);
            String subjName = getCellValue(cellName);
            subjects[i-1] = new Subject(subjCode, subjName);

        }
        wb.close();
        fins.close();
    }
    static String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue()).trim();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim();
            default -> "";
        };
    }
    public static Subject[] getAllSubjects(){
        return subjects;
    }
}
