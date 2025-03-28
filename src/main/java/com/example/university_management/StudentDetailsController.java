package com.example.university_management;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

import static com.example.university_management.deleteStudent.deletingStudent;

public class StudentDetailsController {

    public javafx.scene.control.TextField editStudent1;
    @FXML
    private ListView<String> studentDetails;
    @FXML
    private StackPane profilePic;
    private AdminDash adminDash;
    private Stage stage;
    public Student student;

    // Method to set student details
    public void setStudentDetails(Student student, Stage stage, AdminDash adminDash) {
        this.student = student;
        this.stage = stage;
        this.adminDash = adminDash;

        // Hide the text field initially
        editStudent1.setVisible(false);

        // Clear previous details and display new student information
        studentDetails.getItems().clear();
        studentDetails.getItems().add("Student ID: " + student.studentID);
        studentDetails.getItems().add("Password: " + student.password);
        studentDetails.getItems().add("Name: " + student.name);
        studentDetails.getItems().add("Address: " + student.address);
        studentDetails.getItems().add("Telephone: " + student.telephone);
        studentDetails.getItems().add("Email: " + student.email);
        studentDetails.getItems().add("Academic Level: " + student.academicLevel);
        studentDetails.getItems().add("Current Semester: " + student.currSem);
        studentDetails.getItems().add("Profile Photo: " + student.profilePhoto);
        studentDetails.getItems().add("Subject Registered: " + student.subjRej);
        studentDetails.getItems().add("Thesis: " + student.thesis);
        studentDetails.getItems().add("Progress: " + student.progress);

        // Display profile picture
        String profilePicPath = student.getProfilePhoto();
        if (profilePicPath != null && !profilePicPath.isEmpty()) {
            if (student.profilePhoto.equals("default")) profilePicPath = loginController.defaultImagePath + "profilePic.png";
            profilePic.setStyle("-fx-background-image: url('" + profilePicPath + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-radius: 50%; " +
                    "-fx-min-width: 100px; -fx-min-height: 100px;");
        }

        // Handle clicks on ListView items to edit Address or Telephone
        studentDetails.setOnMouseClicked(event -> {
            String selectedItem = studentDetails.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.contains("Address:")) {
                    editStudent1.setVisible(true);
                    editStudent1.setPromptText("Edit Student Address");
                    editAddress(student);
                } else if (selectedItem.contains("Telephone:")) {
                    editStudent1.setVisible(true);
                    editStudent1.setPromptText("Edit Student Telephone");
                    editTele(student);
                }
            }
        });
    }

    // Close button action
    @FXML
    private void handleClose() {
        Stage stage = (Stage) profilePic.getScene().getWindow();
        stage.close();
    }

    // Edit student address
    private void editAddress(Student student) {
        editStudent1.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String newAddress = editStudent1.getText();
                studentDetails.getItems().set(studentDetails.getItems().indexOf("Address: " + student.address), "Address: " + newAddress);
                EditStudent.editStudent(student.studentID, null, null, newAddress, null);
                editStudent1.clear();
            }
        });
    }

    // Edit student telephone
    private void editTele(Student student) {
        editStudent1.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String newTele = editStudent1.getText();
                studentDetails.getItems().set(studentDetails.getItems().indexOf("Telephone: " + student.telephone), "Telephone: " + newTele);
                EditStudent.editStudent(student.studentID, null, null, null, newTele);
                editStudent1.clear();
            }
        });
    }

    // Method to delete the user
    @FXML
    public void deleteUser() throws IOException {
        if (student == null) return;

        // Delete student from Excel
        deleteStudent.deletingStudent(student.studentID);

        // Close the window
        stage.close();

        // Refresh the AdminDash table
        if (adminDash != null) {
            adminDash.refreshTable(student);
        }
    }
}
