package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReadEvents {
    private static final String FILE_PATH = loginController.filePath;
    private static Event[] events;
    public static void loadEvents() throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(4);
        int rowCount = sheet.getLastRowNum(); // Total students (excluding header)
        events = new Event[rowCount]; // Initialize array with row count
        for (int i = 1; i <= rowCount; i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row == null) continue;
            events[i-1] = new Event(getCellValue(row.getCell(0)), getCellValue(row.getCell(1)), getCellValue(row.getCell(2)), getCellValue(row.getCell(3)),
                    getCellValue(row.getCell(4)), getCellValue(row.getCell(5)), getCellValue(row.getCell(6)), getCellValue(row.getCell(7)),
                    getCellValue(row.getCell(8)));


        }
        wb.close();
        fins.close();
    }
    static String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // If the cell is a date, format it to the desired pattern
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    Date date = cell.getDateCellValue();
                    yield dateFormat.format(date);
                }
                // If it's a numeric value that is not a date, return as numeric string
                yield String.valueOf(cell.getNumericCellValue()).trim();
                // If it's a numeric value that is not a date, return as numeric string
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim();
            default -> "";
        };
    }
    public static Event eventInfo(String eventCode){
        for (Event event: events){
            if (eventCode.equals(event.eventCode)){
                return new Event(event.eventCode, event.eventName, event.description,event.location, event.dateAndTime, event.capacity, event.cost, event.image, event.regStudents);
            }
        }
        return null;
    }
    public static Event[] getAllEvents() { return events;}
}
