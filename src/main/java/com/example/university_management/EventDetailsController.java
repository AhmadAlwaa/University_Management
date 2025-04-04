package com.example.university_management;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EventDetailsController {
    @FXML private  Text errorText;
    @FXML private ListView<String> eventDetailsList;
    @FXML private Button regButton;
    @FXML private ListView<String> regStudents;
    @FXML private Button deleteEvent;
    @FXML private StackPane defaultPic;
    public void setEventDetails(String eventName, AdminDash adminDash) throws IOException {
        regButton.setVisible(!loginController.role.equals("ADMIN"));
        deleteEvent.setVisible(false);
        eventDetailsList.getItems().clear();
        errorText.setVisible(false);
        ReadEvents.loadEvents();
        String profilePicPath;
        Event[] event = ReadEvents.getAllEvents();
        if(loginController.role.equals("ADMIN")){
            deleteEvent.setVisible(true);
            regStudents.setVisible(true);
        }else {
            regButton.setVisible(true);
            regStudents.setVisible(false);
        }
        deleteEvent.setOnAction(MouseEvent -> {
            try {
                Event.deleteEvent(eventName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                adminDash.refreshEvents();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) deleteEvent.getScene().getWindow();
            stage.close();
        });
        for (Event event1: event) {
            if (event1 == null || event1.dateAndTime == null || event1.dateAndTime.trim().isEmpty()) {
                continue;  // Skip if invalid
            }
            if (event1.eventName.equals(eventName)) {
                eventDetailsList.getItems().add("Event Code: " +event1.eventCode);
                eventDetailsList.getItems().add("Event Name: " +eventName);
                eventDetailsList.getItems().add("Event Details: " +event1.description);
                eventDetailsList.getItems().add("Location: " +event1.location);
                eventDetailsList.getItems().add("Date and Time: " +event1.dateAndTime);
                eventDetailsList.getItems().add("Capacity: " +event1.capacity);
                eventDetailsList.getItems().add("Cost: "+event1.cost);
                if (event1.image.equals("default")){
                    profilePicPath = loginController.defaultImagePath + "defaultBackground.png";
                }
                else profilePicPath = event1.image;
                defaultPic.setStyle("-fx-background-image: url('" + profilePicPath + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 134px; -fx-min-height: 100px;");
                List<String> names = Arrays.asList(event1.regStudents.split(","));
                for(String name: names){
                    regStudents.getItems().add(name);
                }

                regButton.setOnMouseClicked(MouseEvent ->{
                    try {
                        if(loginController.role.equals("USER")){
                            if (!(event1.regStudents.contains(Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name))){
                                try {
                                    regStudents.getItems().add(Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name);
                                    EditEvent.editEvent(Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name, event1.eventName);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else{
                                errorText.setVisible(true);
                            }

                        }
                        else if (!(event1.regStudents.contains(Objects.requireNonNull(ReadingFaculties.facultiesInfo(loginController.user)).name))){
                            regStudents.getItems().add(Objects.requireNonNull(ReadingFaculties.facultiesInfo(loginController.user)).name);
                            EditEvent.editEvent(Objects.requireNonNull(ReadingFaculties.facultiesInfo(loginController.user)).name, event1.eventName);
                        }
                        else{
                            errorText.setVisible(true);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }

    }

}
