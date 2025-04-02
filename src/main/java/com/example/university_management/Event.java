package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import javax.imageio.IIOException;
import java.io.*;

public class Event {
    String eventCode;
    String eventName;
    String description;
    String location;
    String dateAndTime;
    String capacity;
    String cost;
    String image;
    String regStudents;
    public Event(String eventCode, String eventName, String description, String location, String dateAndTime, String capacity, String cost, String image, String regStudents){
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.dateAndTime = dateAndTime;
        this.capacity = capacity;
        this.cost = cost;
        this.image = image;
        this.regStudents = regStudents;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getCost() {
        return cost;
    }

    public String getImage() {
        return image;
    }

    public String getRegStudents() {
        return regStudents;
    }
    public static void addEvent(String eventName, String eventDescription, String Location, String dateAndTime, String capacity, String cost) {
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
            Sheet sheet = wb.getSheetAt(4);
            if (sheet == null) {
                return;
            }
            String lastEventCode= "";
            int lastUsedRow = -1; // Track last row with data
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null && !row.getCell(0).getStringCellValue().isEmpty()) {
                    lastEventCode = row.getCell(0).getStringCellValue();
                    lastUsedRow = i;
                }
            }
            int nextNumber = 1;
            if (!lastEventCode.isEmpty()){
                int lastNumber = Integer.parseInt(lastEventCode.substring(4));
                nextNumber = lastNumber + 1;
            }
            String EventCode = "EV00" + nextNumber ;
            int firstEmptyRow = lastUsedRow + 1; // Next available row after last used row
            Row newRow = sheet.createRow(firstEmptyRow);
            newRow.createCell(0).setCellValue(EventCode);
            newRow.createCell(1).setCellValue(eventName);
            newRow.createCell(2).setCellValue(eventDescription);
            newRow.createCell(3).setCellValue(Location);
            newRow.createCell(4).setCellValue(dateAndTime);
            newRow.createCell(5).setCellValue(capacity);
            newRow.createCell(6).setCellValue(cost);
            newRow.createCell(7).setCellValue("default");
            newRow.createCell(8).setCellValue("");
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
    public static void deleteEvent(String eventToDelete) throws IOException {
        final String FILE_PATH = loginController.filePath;
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(4);
        int rowCount = sheet.getLastRowNum();
        int rowIndexToDelete = -1;

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell idCell = row.getCell(1);

            if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().equals(eventToDelete)) {
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

