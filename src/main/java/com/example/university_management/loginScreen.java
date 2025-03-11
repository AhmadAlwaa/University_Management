//class that creates the login screen
package com.example.university_management;
//imports for project
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class loginScreen extends Application {
    @Override
    public void start(Stage stage) throws IOException { //creates login screen
        FXMLLoader fxmlLoader = new FXMLLoader(loginScreen.class.getResource("login.fxml")); //references the fxml called login
        Scene scene = new Scene(fxmlLoader.load(), 700, 500); //open login.fxml with dimensions 700 width and 500 height
        stage.setTitle("University Management System"); //sets the login screen title to university management system
        stage.setScene(scene);
        stage.setResizable(true);//sets the page to not resizable
        stage.show(); //makes the page visible
    }
    public static void main(String[] args) {
        launch();
    }

}