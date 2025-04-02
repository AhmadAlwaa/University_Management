package com.example.university_management;

import java.io.*;

import org.apache.poi.ss.usermodel.*;


public class Student {
    String studentID;
    String password;
    String name;
    String address;
    String telephone;
    String email;
    String academicLevel;
    String currSem;
    String profilePhoto;
    String subjRej;
    String thesis;
    String progress;

    public Student(String studentID, String password, String name, String address, String telephone, String email, String academicLevel, String currSem, String profilePhoto, String subjRej, String thesis, String progress) {
        this.studentID = studentID;
        this.password = password;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.academicLevel = academicLevel;
        this.currSem = currSem;
        this.profilePhoto = profilePhoto;
        this.subjRej = subjRej;
        this.thesis = thesis;
        this.progress = progress;
    }
    public String getStudentID() {
        return studentID;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public String getCurrSem() {
        return currSem;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getSubjRej() {
        return subjRej;
    }

    public String getThesis() {
        return thesis;
    }

    public String getProgress() {
        return progress;
    }
}
class AddStudent extends Student{

    public AddStudent(String studentID, String password, String name, String address, String telephone, String email, String academicLevel, String currSem, String profilePhoto, String subjRej, String thesis, String progress) {
        super(studentID, password, name, address, telephone, email, academicLevel, currSem, profilePhoto, subjRej, thesis, progress);
    }
    private static final String FILE_PATH = loginController.filePath;

    public void addStudent() {
        FileInputStream fis = null;
        Workbook wb = null;
        FileOutputStream fos = null;

        try {
            // Open the existing Excel file
            fis = new FileInputStream(new File(FILE_PATH));
            wb = WorkbookFactory.create(fis);
            fis.close(); // Close input stream after loading the workbook

            // Get the sheet by index (assuming student data is in the third sheet)
            Sheet sheet = wb.getSheetAt(2);
            if (sheet == null) {
                System.out.println("Sheet not found!");
                return;
            }

            // 🔹 Find the last student ID properly (avoid empty rows issue)
            String lastStudentID = "";
            int lastUsedRow = -1; // Track last row with data
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null && !row.getCell(0).getStringCellValue().isEmpty()) {
                    lastStudentID = row.getCell(0).getStringCellValue();
                    lastUsedRow = i;
                }
            }

            // 🔹 Extract the numeric part and increment it
            int nextIDNumber = 1; // Default if no ID is found
            if (!lastStudentID.isEmpty() && lastStudentID.matches("S\\d{8}")) {
                int lastNumber = Integer.parseInt(lastStudentID.substring(1)); // Remove 'S' and parse as int
                nextIDNumber = lastNumber + 1; // Increment ID
            }

            // 🔹 Generate the new student ID (SYYYYNNNN format)
            String studentID = "S" + nextIDNumber;

            // 🔹 Find the first empty row correctly
            int firstEmptyRow = lastUsedRow + 1; // Next available row after last used row
            Row newRow = sheet.createRow(firstEmptyRow);

            // Default values

            // Add student data
            newRow.createCell(0).setCellValue(studentID);      // Student ID
            newRow.createCell(1).setCellValue(this.name);           // Name
            newRow.createCell(2).setCellValue(this.address);        // Address
            newRow.createCell(3).setCellValue(this.telephone);        // Telephone
            newRow.createCell(4).setCellValue(this.email);          // Email
            newRow.createCell(5).setCellValue(this.academicLevel);  // Academic Level
            newRow.createCell(6).setCellValue(this.currSem);// Current Semester
            newRow.createCell(7).setCellValue(this.profilePhoto);   // Profile Photo
            newRow.createCell(8).setCellValue(this.subjRej); // Subjects Registered
            newRow.createCell(9).setCellValue(this.thesis);    // Thesis Title
            newRow.createCell(10).setCellValue(this.progress);      // Progress
            newRow.createCell(11).setCellValue(this.password); // Password

            // Save changes to the Excel file
            fos = new FileOutputStream(new File(FILE_PATH));
            wb.write(fos);
            fos.flush();

            System.out.println("Student added successfully! Student ID: " + studentID);
            this.studentID = studentID;

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
    public static void addEnrollment(String student, String subjectCode) throws IOException {
        File file = new File(FILE_PATH);
        FileInputStream fins = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fins);
        Sheet sheet = wb.getSheetAt(2);
        fins.close(); // Close input stream after reading

        boolean studentFound = false;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue; // Skip null rows

            Cell cell = row.getCell(1);
            if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(student)) {
                Cell subjectCell = row.getCell(8);
                if (subjectCell == null) {
                    subjectCell = row.createCell(8, CellType.STRING);
                    subjectCell.setCellValue(subjectCode);
                } else {
                    String cellBefore = subjectCell.getStringCellValue();
                    subjectCell.setCellValue(cellBefore + ", " + subjectCode);
                }
                studentFound = true;
                break; // Stop once the student is found
            }
        }

        if (!studentFound) {
            int newRowIdx = sheet.getLastRowNum() + 1;
            Row newRow = sheet.createRow(newRowIdx);
            newRow.createCell(1, CellType.STRING).setCellValue(student);
            newRow.createCell(8, CellType.STRING).setCellValue(subjectCode);
        }

        FileOutputStream fout = new FileOutputStream(file);
        wb.write(fout); // Save changes
        fout.close();
        wb.close();
    }


}
