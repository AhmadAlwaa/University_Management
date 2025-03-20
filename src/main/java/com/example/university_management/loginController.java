//Controller for login page
package com.example.university_management;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class loginController {
    private static final Logger log = LogManager.getLogger(loginController.class);
    public static String role = "USER"; // Public role variable set to USER by default
    public static String user; // Make user static variable called user
    public static String filePath = "C:\\Users\\Ahmad\\Downloads\\UMS_Data.xlsx";
    @FXML  private Button login; //references to the button in login.fxml called login
    @FXML private AnchorPane anchorPane;
    @FXML private ImageView image;
    @FXML private BorderPane loginPage;
    @FXML
    void hover(MouseEvent event) { //when the button login is hovered over it called the hover function (this is set in login.fxml)
        login.setStyle("-fx-background-color: #2980b9;"); //set color to blue
        login.setScaleX(1.1); //scale up the width by times 1.1
        login.setScaleY(1.1); // scale up the height by 1.1 times
    }

    @FXML
    void hoverExit(MouseEvent event) { //when you are not hovering over the login button
        login.setStyle("-fx-background-color:  #FF715E;"); //set color to red
        login.setScaleX(1.0);
        login.setScaleY(1.0);
    }

    @FXML  private TextField ID; //reference to text field called id
    @FXML private Text incorrectID; //reference to text called incorrectID
    @FXML private PasswordField passWord; //reference to passwordField called password

    @FXML
    void mouseDown(javafx.event.ActionEvent event) throws IOException {  //when the login button is clicked this function is called
        String enteredID = ID.getText(); //set id to what is entered in the id text field
        String enteredPass = passWord.getText(); // set password from what is entered into password field
        Reading_Student.loadStudentCredentials();
        ReadingFaculties.loadFaculties();
        if (Reading_Student.verifyLogin(enteredID, enteredPass)) { //called class reading student and function verify login in class passing in entered id and password and check is returned value is true
            user = enteredID; //set variable user to the entered userID
            Stage stage = (Stage) login.getScene().getWindow(); //get current scene using the login button
            DashBoard dashboard = new DashBoard(); //start new window using class dashboard
            dashboard.start(stage); //show the stage in place of the current stage gotten from login button
        }else if(enteredID.equals("ADMIN") && enteredPass.equals("admin1")){ //else check if the entered id and password are for admin
            role = "ADMIN"; //set role to admin
            Stage stage = (Stage) login.getScene().getWindow();
            DashBoardAdmin dashboardAdmin = new DashBoardAdmin(); //create the admin dashboard
            dashboardAdmin.start(stage);
        } else if(ReadingFaculties.verifyLogin(enteredID,enteredPass)){
            user = enteredID;
            role = "FACULTY";
            Stage stage = (Stage) login.getScene().getWindow();
            DashBoard dashboard = new DashBoard();
            dashboard.start(stage);
        }
        else {
            incorrectID.setText("Incorrect Username or Password"); //otherwise say incorrect username or password
        }
    }
    @FXML
    public void initialize() {
        // Listener for width changes
        loginPage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            //image.setFitWidth(newWidth.doubleValue()/3.5); // Resize width
            //image.setLayoutX((newWidth.doubleValue() - image.getFitWidth()) / 2 +25);
            loginPage.setMinWidth(newWidth.doubleValue()/3.5);

        });

        // Listener for height changes
        loginPage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            //image.setFitHeight(newHeight.doubleValue()/1.3); // Resize height
            loginPage.setMinHeight(newHeight.doubleValue()/3.5);
            //image.setLayoutY((newHeight.doubleValue() - image.getFitHeight()) +50);
        });
    }
}