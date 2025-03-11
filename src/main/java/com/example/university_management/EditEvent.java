package com.example.university_management;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import java.io.*;
public class EditEvent {
    private static final String FILE_PATH = loginController.filePath;
    public static void editEvent(String userName, String eventName){
        FileInputStream fis = null;
        Workbook wb = null;
        FileOutputStream fos = null;

        try {
            // Open the existing Excel file
            fis = new FileInputStream(new File(FILE_PATH));
            wb = WorkbookFactory.create(fis);
            fis.close(); // Close input stream after loading the workbook

            // Get the sheet where student data is stored
            Sheet sheet = wb.getSheetAt(4);
            if (sheet == null) {
                System.out.println("Sheet not found!");
                return;
            }

            boolean userNameFound = false;

            // Search for the student ID
            for (Row row : sheet) {
                Cell idCell = row.getCell(1); // Student ID is in column 0
                if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().contains(eventName)) {
                    row.getCell(8).setCellValue(row.getCell(8).getStringCellValue() + ", " + userName);

                    userNameFound = true;
                    break;
                }
            }

            if (userNameFound) {
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
