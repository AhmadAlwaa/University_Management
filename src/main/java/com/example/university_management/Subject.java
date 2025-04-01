package com.example.university_management;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Subject {
    String code;
    String name;
    public Subject(String code, String name){
        this.code = code;
        this.name = name;
    }
    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public String toString() {
        return code + " - " + name;
    }
    public static void addSubject(Subject subject){
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
            Sheet sheet = wb.getSheetAt(0);
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



            // 🔹 Find the first empty row correctly
            int firstEmptyRow = lastUsedRow + 1; // Next available row after last used row
            Row newRow = sheet.createRow(firstEmptyRow);

            // Default values

            // Add student data
            newRow.createCell(0).setCellValue(subject.code);      // Student ID
            newRow.createCell(1).setCellValue(subject.name);           // Name

            // Save changes to the Excel file
            fos = new FileOutputStream(new File(FILE_PATH));
            wb.write(fos);
            fos.flush();


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
