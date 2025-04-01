package com.example.university_management;

import org.apache.poi.ss.usermodel.*;

import java.io.*;

public class Course {
    String courseCode;
    String courseName;
    String subjectCode;
    String sectionNumber;
    String capacity;
    String lectureTime;
    String finalExamDate;
    String location;
    String teacherName;
    public Course(String courseCode, String courseName, String subjectCode, String sectionNumber, String capacity, String lectureTime, String finalExamDate, String location, String teacherName){
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.subjectCode = subjectCode;
        this.sectionNumber = sectionNumber;
        this. capacity = capacity;
        this.lectureTime = lectureTime;
        this.finalExamDate = finalExamDate;
        this.location = location;
        this.teacherName = teacherName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public String getFinalExamDate() {
        return finalExamDate;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacherName() {
        return teacherName;
    }
    public static void deleteCourse(String courseCode) throws IOException {
        String FILE_PATH = loginController.filePath;
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(1);
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
            Cell idCell = row.getCell(1);

            if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().equals(courseCode)) {
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
    public static void addCourse(Course course) throws IOException {
        String FILE_PATH = loginController.filePath;
        FileInputStream fins = new FileInputStream(new File(FILE_PATH));
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(1);

        // Find the next empty row
        int lastUsedRow = -1; // Track last row with data
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null && !row.getCell(1).getStringCellValue().isEmpty()) {
                lastUsedRow = i;
            }
        }
        Row row = sheet.createRow(lastUsedRow);

        row.createCell(0).setCellValue(course.getCourseCode());
        row.createCell(1).setCellValue(course.getCourseName());
        row.createCell(2).setCellValue(course.getSubjectCode());
        row.createCell(3).setCellValue(course.getSectionNumber());
        row.createCell(4).setCellValue(course.getCapacity());
        row.createCell(5).setCellValue(course.getLectureTime());
        row.createCell(6).setCellValue(course.getFinalExamDate());
        row.createCell(7).setCellValue(course.getLocation());
        row.createCell(8).setCellValue(course.getTeacherName());

        fins.close();

        // Write changes to file
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            wb.write(fos);
        }

        wb.close();
    }

}
