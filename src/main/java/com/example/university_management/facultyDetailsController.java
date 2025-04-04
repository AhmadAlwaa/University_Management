package com.example.university_management;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.IOException;

public class facultyDetailsController {
    @FXML ListView<String> facultyList;
    private AdminDash adminDash;
    public static String facultyID;
    public void setFacultyDetails(String facultyName, AdminDash adminDash) throws IOException {
        this.adminDash = adminDash;
        facultyList.getItems().clear();
        Faculties faculties = ReadingFaculties.facultiesInfo(facultyName);
        assert faculties != null;
        facultyID = faculties.ID;
        facultyList.getItems().add("Faculty ID: " + faculties.ID);
        facultyList.getItems().add("Faculty Name: " + faculties.name);
        facultyList.getItems().add("Degree: " + faculties.degree);
        facultyList.getItems().add("Research Interest: " + faculties.researchInterest);
        facultyList.getItems().add("Email: " + faculties.email);
        facultyList.getItems().add("Office Location: " + faculties.officeLocation);
        facultyList.getItems().add("Courses Offered: " + faculties.coursesOffered);
        facultyList.getItems().add("Password: " + faculties.password);
    }
    @FXML
    public void deleteFaculty() throws IOException {
        DeleteFaculty.deleteFaculty(facultyID);
        adminDash.faculty();

    }
}
