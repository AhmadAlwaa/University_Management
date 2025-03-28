package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Faculties {
    String ID;
    String name;
    String degree;
    String researchInterest;
    String email;
    String officeLocation;
    String coursesOffered;
    String password;
    public Faculties(String ID, String name, String degree, String researchInterest, String email, String officeLocation, String coursesOffered, String password){
        this.ID = ID;
        this.name = name;
        this.degree = degree;
        this.researchInterest = researchInterest;
        this.email = email;
        this.officeLocation = officeLocation;
        this.coursesOffered = coursesOffered;
        this.password = password;
    }
    public static void addFaculty(Faculties faculty){
        String FILE_PATH = loginController.filePath;
        FileInputStream fis = null;
        Workbook wb = null;
        FileOutputStream fos = null;

        try {
            // Open the existing Excel file
            fis = new FileInputStream(new File(FILE_PATH));
            wb = WorkbookFactory.create(fis);
            fis.close(); // Close input stream after loading the workbook

            // Get the sheet by index (assuming student data is in the third sheet)
            Sheet sheet = wb.getSheetAt(3);
            if (sheet == null) {
                return;
            }
            int lastUsedRow = -1; // Track last row with data
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null && !row.getCell(0).getStringCellValue().isEmpty()) {
                    lastUsedRow = i;
                }
            }
            int firstEmptyRow = lastUsedRow + 1; // Next available row after last used row
            Row newRow = sheet.createRow(firstEmptyRow);
            newRow.createCell(0).setCellValue(faculty.ID);
            newRow.createCell(1).setCellValue(faculty.name);
            newRow.createCell(2).setCellValue(faculty.degree);
            newRow.createCell(3).setCellValue(faculty.researchInterest);
            newRow.createCell(4).setCellValue(faculty.email);
            newRow.createCell(5).setCellValue(faculty.officeLocation);
            newRow.createCell(6).setCellValue(faculty.coursesOffered);
            newRow.createCell(7).setCellValue(faculty.password);
            fos = new FileOutputStream(new File(FILE_PATH));
            wb.write(fos);
            fos.flush();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (wb != null) wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class DeleteFaculty{
    private static final String FILE_PATH = loginController.filePath;

    public static void deleteFaculty(String facultyID) throws IOException {
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
        int rowIndexToDelete = -1;

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell idCell = row.getCell(0);

            if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().equals(facultyID)) {
                rowIndexToDelete = row.getRowNum();
                break;
            }
        }

        if (rowIndexToDelete != -1) {
            sheet.removeRow(sheet.getRow(rowIndexToDelete));

            // Shift remaining rows up
            for (int i = rowIndexToDelete; i < rowCount; i++) {
                Row currentRow = sheet.getRow(i + 1);
                if (currentRow != null) {
                    Row newRow = sheet.createRow(i);
                    for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                        Cell oldCell = currentRow.getCell(j);
                        if (oldCell != null) {
                            Cell newCell = newRow.createCell(j, oldCell.getCellType());
                            switch (oldCell.getCellType()) {
                                case STRING -> newCell.setCellValue(oldCell.getStringCellValue());
                                case NUMERIC -> newCell.setCellValue(oldCell.getNumericCellValue());
                            }
                        }
                    }
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            wb.write(fos);
        }
        fins.close();
        wb.close();
    }
}