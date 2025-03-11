package com.example.university_management;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import java.io.*;
public class EditStudent {
    private static final String FILE_PATH = loginController.filePath;

    public static void editStudent(String studentID, String newPassword, String newProfile, String newAddress, String newContact) {
        FileInputStream fis = null;
        Workbook wb = null;
        FileOutputStream fos = null;

        try {
            // Open the existing Excel file
            fis = new FileInputStream(new File(FILE_PATH));
            wb = WorkbookFactory.create(fis);
            fis.close(); // Close input stream after loading the workbook

            // Get the sheet where student data is stored
            Sheet sheet = wb.getSheetAt(2);
            if (sheet == null) {
                System.out.println("Sheet not found!");
                return;
            }

            boolean studentFound = false;

            // Search for the student ID
            for (Row row : sheet) {
                Cell idCell = row.getCell(0); // Student ID is in column 0
                if (idCell != null && idCell.getCellType() == CellType.STRING &&
                        idCell.getStringCellValue().equals(studentID)) {
                    if (newPassword != null) row.getCell(11).setCellValue(newPassword);
                    // Update address and contact number// Address (Column 2)
                    if (newAddress != null )row.getCell(2).setCellValue(newAddress); // Telephone (Column 3)
                    if (newContact != null )row.getCell(3).setCellValue(newContact);
                    if (newProfile != null) row.getCell(7).setCellValue(newProfile);
                    studentFound = true;
                    break;
                }
            }

            if (studentFound) {
                // Save changes to the Excel file
                fos = new FileOutputStream(new File(FILE_PATH));
                wb.write(fos);
                fos.flush();
                System.out.println("Student details updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (IOException e) {
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
