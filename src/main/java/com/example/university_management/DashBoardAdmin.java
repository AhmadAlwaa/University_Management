package com.example.university_management;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardAdmin {
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(loginScreen.class.getResource("dashBoardAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 750);
        stage.setTitle("University Management System");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
