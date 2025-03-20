package com.example.university_management;
// Package declaration for the University Management System application

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class DashBoard extends Application {

    // Override the start method to initialize the JavaFX application window
    public void start(Stage stage) throws IOException {
        // Load the FXML layout file for the dashboard interface
        FXMLLoader fxmlLoader = new FXMLLoader(loginScreen.class.getResource("DashBoard.fxml"));
        // Create a scene with the loaded FXML and set its dimensions (900x750)
        Scene scene = new Scene(fxmlLoader.load(), 900, 750);
        // Set the title of the application window
        stage.setTitle("University Management System");
        // Attach the scene to the stage and display the window
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
