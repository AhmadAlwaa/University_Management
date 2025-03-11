package com.example.university_management;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EventDetailsController {
    @FXML private  Text errorText;
    @FXML private ListView<String> eventDetailsList;
    @FXML private Button regButton;
    public void setEventDetails(String eventName) throws IOException {
        regButton.setVisible(!loginController.role.equals("ADMIN"));
        eventDetailsList.getItems().clear();
        errorText.setVisible(false);
        ReadEvents.loadEvents();
        Event[] event = ReadEvents.getAllEvents();
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
                eventDetailsList.getItems().add("Image: " +event1.image);
                eventDetailsList.getItems().add("Registered Students: " +event1.regStudents);
                regButton.setOnMouseClicked(MouseEvent ->{
                    try {
                        if(loginController.role.equals("USER")){
                            if (!(event1.regStudents.contains(Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name))){
                                try {
                                    eventDetailsList.getItems().set(8,"Registered Students: " +  event1.regStudents + ", " + Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name);
                                    EditEvent.editEvent(Objects.requireNonNull(Reading_Student.studentInfo(loginController.user)).name, event1.eventName);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else{
                                errorText.setVisible(true);
                            }

                        }
                        if (!(event1.regStudents.contains(Objects.requireNonNull(ReadingFaculties.facultiesInfo(loginController.user)).name))){
                            eventDetailsList.getItems().set(8,"Registered Students: " +  event1.regStudents + ", " + Objects.requireNonNull(ReadingFaculties.facultiesInfo(loginController.user)).name);
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
