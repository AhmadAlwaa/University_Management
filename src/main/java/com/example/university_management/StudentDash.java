package com.example.university_management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.fxml.Initializable;

import javax.swing.text.LabelView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.example.university_management.ReadingFaculties.facultiesInfo;
import static com.example.university_management.ReadingFaculties.getAllFaculty;
import static com.example.university_management.Reading_Student.getAllStudents;
import static com.example.university_management.Reading_Student.studentInfo;

public class StudentDash implements Initializable {
    ZonedDateTime dateFocus;
    public static String userName; //username (student id)
    ZonedDateTime today;
    @FXML private Label totalEvents;
    @FXML private Label totalFaculty;
    @FXML private Label studentCountLabel;
    @FXML private Label totalCourses;
    @FXML private Rectangle hide;
    @FXML private TableView<Course> courseInfo; //creates table that takes information that is type of course
    @FXML private Text month; //links to text called month on dashboard.fxml
    @FXML private  Text year; //links to text called year
    @FXML private TabPane tabs; //links too TabPane called tabs
    @FXML private Button collapseButton; //links to button called collapse button (closes tabs)
    @FXML private FlowPane calendar; //links to flowPane called calendar
    @FXML private TableColumn<Course, String> courseCode; //all of these and bellow are different columns of the table
    @FXML private TableColumn<Course, String> courseName;
    @FXML private TableColumn<Course, String> subjectCode;
    @FXML private TableColumn<Course, String> sectionNumber;
    @FXML private TableColumn<Course, String> capacity;
    @FXML private TableColumn<Course, String> lectureTime;
    @FXML private TableColumn<Course, String> finalExam;
    @FXML private TableColumn<Course, String> location;
    @FXML private TableColumn<Course, String> teacherName;
    @FXML private ListView<String> listView; //creates a list that is type of information string
    private static int counter =0; //saves the state of tabs (if they are open or closed)
    @FXML private StackPane profilePic;  // StackPane for the profile picture
    @FXML private Button changePic;  // Button to trigger the profile picture change
    @FXML private ListView<String> subjectList;
    @FXML private TextField changePass;
    @FXML private ListView<String> facultyList;
    public void createProfilePic(){ //this function is called when the button to change the profile picture on the student tab is clicked
        changePic.setOnAction(actionEvent -> openFileChooser()); //opens when file chooser when change picture button is clicked
    }

    public void create() throws IOException {
        // Load student credentials from the Excel file
        Reading_Student.loadStudentCredentials();
        if (loginController.role.equals("FACULTY")){
            changePass.setVisible(false);
            changePic.setVisible(false);
            listView.getItems().clear();
            Student[] student = getAllStudents();
            for (Student student1: student){
                if (student1.studentID.isEmpty()){
                    continue;
                }
                listView.getItems().add("Student ID: " + student1.studentID + "            Student Name: " + student1.name);
            }
        } else{
            changePass.setVisible(false); //makes the text field that you enter your new password invisible
            // Get the logged-in user
            String userID = loginController.user;

            // Check if userID is null or empty
            if (userID == null || userID.isEmpty()) {
                listView.getItems().add("No user logged in.");
                return; // Exit early if userID is invalid
            }

            // Retrieve the student info for the logged-in user
            Student student = studentInfo(userID);
            assert student != null; //checks if student is not equal to null
            userName = student.name; //sets username to the name of the student
            // If the student exists, display their info in the ListView
            listView.getItems().clear();  // Clear previous info
            listView.getItems().add("Student ID: " + student.studentID); //sets all rows in the list
            listView.getItems().add("Password: " + student.password);
            listView.getItems().add("Name: " + student.name);
            listView.getItems().add("Address: " + student.address);
            listView.getItems().add("Telephone: " + student.telephone);
            listView.getItems().add("Email: " + student.email);
            listView.getItems().add("Academic Level: " + student.academicLevel);
            listView.getItems().add("Current Semester: " + student.currSem);
            listView.getItems().add("Profile Photo: " + student.profilePhoto);
            listView.getItems().add("Subject Registered: " + student.subjRej);
            listView.getItems().add("Thesis: " + student.thesis);
            listView.getItems().add("Progress: " + student.progress);
            String filePath;
            //checks if student profile photo is set to default in Excel then loads in the default profile picture
            if (student.profilePhoto.equals("default"))
                filePath = "file:/C:/Users/Ahmad/IdeaProjects/University_Management/src/main/resources/profilePic.png";
            else filePath = student.profilePhoto; //else sets the profile photo to the filepath in Excel
            profilePic.setStyle("-fx-background-image: url('" + filePath + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-radius: 50%; " +
                    "-fx-min-width: 100px; -fx-min-height: 100px;");
            listView.setOnMouseClicked(event -> { //waits until listview is clicked on
                String selectedItem = listView.getSelectionModel().getSelectedItem(); //gets the selected row that is clicked on
                if (selectedItem != null && selectedItem.contains("Password:")) { //if the row that is selected is the password column
                    passwordChange(student); //calls function password change passing in all the student information
                }
            });
        }
    }

    private void openFileChooser() {
        // Create a FileChooser to allow the user to select an image file
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters for images (optional)
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog and get the selected file
        Stage currentStage = (Stage) listView.getScene().getWindow();  // Use the current window
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if (selectedFile != null) {
            try {
                // Convert the file path to URI
                String filePath = selectedFile.toURI().toString();
                EditStudent.editStudent(loginController.user,null, filePath, null, null); //calls function edit student in edit student class

                // Set the background image of the profilePic StackPane
                profilePic.setStyle("-fx-background-image: url('" + filePath + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 100px; -fx-min-height: 100px;");
            } catch (Exception e) {
                e.printStackTrace();  // Handle file load error if any
            }
        }
    }
    private void passwordChange(Student student){
        changePass.setVisible(true); //makes the change pass text field visible
        changePass.setOnKeyPressed(keyEvent -> { //waits until the button is enter is pressed
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String newPassword = changePass.getText(); //gets the password entered the text field
                listView.getItems().set(listView.getItems().indexOf("Password: " + student.password), "Password: " + newPassword); //changes the password in the list
                EditStudent.editStudent(student.studentID, newPassword, null, null, null); //changes the password in the Excel
                changePass.clear();
                changePass.setVisible(false);
            }
        });
    }
    public void loadSubject() throws IOException {
        subjectList.getItems().clear(); // Clear existing items
        ReadingSubject.loadSubjects(); // Load subjects from Excel
        Subject[] subjects = ReadingSubject.getAllSubjects(); // Retrieve subjects

        if (subjects == null || subjects.length == 0) {
            subjectList.getItems().add("No subjects available.");
            return;
        }

        for (Subject subj : subjects) {
            if (subj != null) {
                subjectList.getItems().add(subj.getCode() + " - " + subj.getName()); // Display formatted subject info in the subject list
            }
        }
    }
    public void collapseMenu(){ //closes and opens tabs when the collapse menu button is pressed
        if(counter ==0) {
            hide.setVisible(true);
            counter+=1;
            collapseButton.setText("Open Menu");

        }
        else{
            hide.setVisible(false);
            counter = 0;
            collapseButton.setText("Collapse Menu");
        }

    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        dateFocus = ZonedDateTime.now(); //sets dateFocus to the current date
        today = ZonedDateTime.now();
        try { //creates calendar
            drawCalendar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void backOneMonth() throws IOException { //goes back one month
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    @FXML
    void forwardOneMonth() throws IOException { //goes forward one month
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    private void drawCalendar() throws IOException {
        year.setText(String.valueOf(dateFocus.getYear())); //sets year text to current year
        month.setText(String.valueOf(dateFocus.getMonth())); //sets month text to current month

        double calendarWidth = calendar.getPrefWidth(); //gets the calendar width
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1; //sets the calendar stroke width boxes to 1
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) { //creates day and weeks
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    //sets the rectangle of current day to blue
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", \n" +calendarActivities.get(k).getServiceNo() + ", \n" + calendarActivities.get(k).getDate().toLocalTime());
            text.setWrappingWidth(rectangleWidth * 0.8);
            calendarActivityBox.getChildren().add(text);
            int finalK1 = k;
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
                try {
                    // Load FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("eventDetails.fxml"));
                    Parent root = loader.load();

                    // Get controller and pass student data
                    EventDetailsController controller = loader.getController();
                    Stage detailsStage = new Stage();
                    controller.setEventDetails(calendarActivities.get(finalK1).getClientName());

                    // Setup stage
                    detailsStage.setTitle("Event Details");
                    detailsStage.setScene(new Scene(root));
                    detailsStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) throws IOException {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        ReadEvents.loadEvents();
        Event[] event = ReadEvents.getAllEvents();
        for (Event event1 : event) {
            // Ensure event1 is not null and its dateAndTime is valid
            if (event1 == null || event1.dateAndTime == null || event1.dateAndTime.trim().isEmpty()) {
                continue;  // Skip if invalid
            }

            // Parse the event's dateAndTime into LocalDateTime first
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(event1.dateAndTime, formatter);

            // Convert LocalDateTime to ZonedDateTime by adding a time zone
            ZonedDateTime eventDateTime = localDateTime.atZone(ZoneId.systemDefault()); // Adjust time zone as needed

            // Extract the month and year from the event's date and the dateFocus
            int eventMonth = eventDateTime.getMonthValue();  // Get the event's month (1-12)
            int eventYear = eventDateTime.getYear();         // Get the event's year

            int focusMonth = dateFocus.getMonthValue();     // Get the focus month's value
            int focusYear = dateFocus.getYear();            // Get the focus year

            // Check if the event's month and year match the month and year of dateFocus
            if (eventMonth == focusMonth && eventYear == focusYear) {
                // If the event is in the same month as dateFocus, add it to the calendar
                ZonedDateTime time = getDateTime(dateFocus, event1, formatter);
                calendarActivities.add(new CalendarActivity(time, event1.eventName, event1.location));
            }
        }
        return createCalendarMap(calendarActivities);
    }

    private static ZonedDateTime getDateTime(ZonedDateTime dateFocus, Event event1, DateTimeFormatter formatter) {
        LocalDateTime dateTime = LocalDateTime.parse(event1.dateAndTime.trim(), formatter);
        int day = dateTime.getDayOfMonth();
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();
        ZoneId zone = dateFocus.getZone();
        return ZonedDateTime.of(year, month,day, hour, minute, second,0,zone);
    }
    @FXML
    private void loadCourseInfo() throws IOException {
        ReadingCourses.loadCourses();
        courseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        subjectCode.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        sectionNumber.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        lectureTime.setCellValueFactory(new PropertyValueFactory<>("lectureTime"));
        finalExam.setCellValueFactory(new PropertyValueFactory<>("finalExamDate"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        teacherName.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        Course[] courses = ReadingCourses.getAllCourseInfo();
        String userID;
        String rejSubject;
        String[] subjectList;

        if (loginController.role.equals("FACULTY")) {
            userID = Objects.requireNonNull(facultiesInfo(loginController.user)).ID;
            rejSubject = Objects.requireNonNull(facultiesInfo(userID)).coursesOffered;
            subjectList = rejSubject.split(", ");
            for (Course course: courses) {
                if (course.courseName.equals(rejSubject)) {
                    courseList.add(course);
                }
                else{
                    course.location = "";
                    course.teacherName = "";
                    course.finalExamDate ="";
                    course.lectureTime ="";
                    course.capacity ="";
                    courseList.add(course);
                }
            }


        } else {
            userID = Objects.requireNonNull(studentInfo(loginController.user)).studentID;
            rejSubject = Objects.requireNonNull(studentInfo(userID)).subjRej;
            subjectList = rejSubject.split(", ");
            String check = "not found";
            for (Course course: courses) {
                for(String subject: subjectList) {
                    if (course.subjectCode.equals(subject)) {
                        check = "found";
                        break;
                    }
                }
                if (check.equals("found")) {
                    courseList.add(course);
                } else {
                    course.location = "";
                    course.teacherName = "";
                    course.finalExamDate = "";
                    course.lectureTime = "";
                    course.capacity = "";
                    courseList.add(course);
                }
                check = "not found";
            }
        }
        courseInfo.setItems(courseList);
// Row Factory for custom styling
        courseInfo.setRowFactory(tv -> new TableRow<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (course == null || empty) {
                    setStyle(""); // Reset default style for empty rows
                } else {
                    boolean isRejected = false; // Flag to check if the course is rejected

                    for (String courseCheck : subjectList) {

                        if (course.subjectCode.equals(courseCheck) || course.courseName.equals(courseCheck)) {
                            isRejected = true; // Mark the course as rejected
                            break; // No need to check further once found
                        }
                    }

                    if (isRejected) {
                        setStyle("-fx-background-color: #e48487;"); // Red color for rejected courses
                    } else {
                        setStyle(""); // Default styling
                    }
                }
            }
        });

    }
    @FXML
    private void faculty() throws IOException {
        ReadingFaculties.loadFaculties();

        if (loginController.role.equals("FACULTY")){
            Faculties faculties = facultiesInfo(loginController.user);
            facultyList.getItems().clear();
            assert faculties != null;
            facultyList.getItems().add("Faculty ID: " + faculties.ID);
            facultyList.getItems().add("Name: " + faculties.name);
            facultyList.getItems().add("Degree: " + faculties.degree);
            facultyList.getItems().add("Research Interest: " + faculties.researchInterest);
            facultyList.getItems().add("Email: " + faculties.email);
            facultyList.getItems().add("Office Location: " + faculties.officeLocation);
            facultyList.getItems().add("Courses Offered: " + faculties.coursesOffered);
            facultyList.getItems().add("Password: " + faculties.password);
        }
        else {
            ReadingCourses.loadCourses();
            String userID = Objects.requireNonNull(studentInfo(loginController.user)).studentID;
            String rejSubject = Objects.requireNonNull(studentInfo(userID)).subjRej;
            String[] subjectList = rejSubject.split(", ");
            Faculties[] faculties = getAllFaculty();
            Course[] courses = ReadingCourses.getAllCourseInfo();

            facultyList.getItems().clear();
            assert faculties != null;
            int arrayLength=0;
            int count = 0;
            System.out.println(Arrays.toString(subjectList));
            for(Course courseCount: courses){
                for (String subject2: subjectList){
                    if (courseCount.subjectCode.equals(subject2)){
                        arrayLength+=1;
                    }
                }
            }
            String[] subjectToCourse = new String[arrayLength];
            String[] sectionCount = new String[arrayLength];
            for (String subjectsInList : subjectList) {
                System.out.println(subjectsInList);
                for (Course subjectToCourses1 : courses) {
                    if (subjectToCourses1.subjectCode.equals(subjectsInList)) {
                        subjectToCourse[count] = subjectToCourses1.courseName;
                        sectionCount[count] = subjectToCourses1.sectionNumber;
                        count +=1;
                    }
                }
            }
            int sectionCountLoop = 0;
            for(String coursesInList: subjectToCourse){
                for(Faculties faculties1: faculties){
                    if(faculties1 == null) continue;
                    if (faculties1.coursesOffered.equals(coursesInList)){
                        facultyList.getItems().add("Course: " + faculties1.coursesOffered + "      " + sectionCount[sectionCountLoop]);
                        facultyList.getItems().add("Prof Name: " + faculties1.name);
                        facultyList.getItems().add("Prof Email: " + faculties1.email);
                        facultyList.getItems().add("Office Location: " + faculties1.officeLocation);
                        facultyList.getItems().add("Research Interest: " + faculties1.researchInterest);
                    }
                }
                sectionCountLoop +=1;
            }
            facultyList.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(item);
                        // Check if the item contains "Course"
                        if (item.contains("Course")) {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                        } else {
                            setStyle(null); // Reset style for other items
                        }
                    }
                }
            });
        }

    }
    @FXML
    private void countingStudents() throws IOException {
        Reading_Student.loadStudentCredentials();
        Student[] list = Reading_Student.getAllStudents();
        int count1 =0;
        for (Student student1: list){
           if (!(student1.name.isEmpty())){
               count1+=1;
           }
        }
        studentCountLabel.setText(String.valueOf(count1));
        ReadingCourses.loadCourses();
        Course[] list1 = ReadingCourses.getAllCourseInfo();
        int count2 = 0;
        for(Course course: list1){
            if(!(course.courseName.isEmpty())){
                count2+=1;
            }
        }
        totalCourses.setText(String.valueOf(count2));
        ReadingFaculties.loadFaculties();
        Faculties[] list2 = ReadingFaculties.getAllFaculty();
        int count3 = 0;
        for (Faculties faculties: list2){
            if (faculties == null)continue;
            if(!(faculties.ID.isEmpty())) count3+=1;
        }
        totalFaculty.setText(String.valueOf(count3));
        ReadEvents.loadEvents();
        Event[] list3 = ReadEvents.getAllEvents();
        int count4 = 0;
        for(Event event: list3){
            if (event == null) continue;
            if(!(event.eventName.isEmpty())) count4 +=1;
        }
        totalEvents.setText(String.valueOf(count4));
    }
    @FXML
    private void logOut() throws IOException {
        Stage stage = (Stage) collapseButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(loginScreen.class.getResource("login.fxml")); //references the fxml called login
        Scene scene = new Scene(fxmlLoader.load(), 700, 500); //open login.fxml with dimensions 700 width and 500 height
        stage.setTitle("University Management System"); //sets the login screen title to university management system
        stage.setScene(scene);
        stage.show();

    }

}


