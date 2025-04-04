package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import java.io.*;

public class deleteStudent {
    private static final String FILE_PATH = loginController.filePath;

    public static void deletingStudent(String studentID) throws IOException {
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(2);
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

            if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().equals(studentID)) {
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
