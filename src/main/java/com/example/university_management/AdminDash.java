package com.example.university_management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdminDash implements Initializable {
    @FXML private TableView<Course> courseInfo;
    @FXML private Button collapseButton;
    @FXML private Label totalEvents;
    @FXML private Label totalFaculty;
    @FXML private Label studentCountLabel;
    @FXML private Label totalCourses;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> colmStudentID;
    @FXML
    private TableColumn<Student, String> colmStudentName;
    @FXML
    private TableColumn<Student, String> colmStudentEmail;
    @FXML private ListView<String> adminSubjects;
    @FXML private Button deleteSubjectBtn;
    @FXML private TabPane tabs;
    private static int counter = 0;
    ZonedDateTime today;
    ZonedDateTime dateFocus;
    @FXML private Text month; //links to text called month on dashboard.fxml
    @FXML private  Text year;
    @FXML private FlowPane calendar;
    @FXML private TableColumn<Course, String> courseCode; //all of these and bellow are different columns of the table
    @FXML private TableColumn<Course, String> courseName;
    @FXML private TableColumn<Course, String> subjectCode;
    @FXML private TableColumn<Course, String> sectionNumber;
    @FXML private TableColumn<Course, String> capacity;
    @FXML private TableColumn<Course, String> lectureTime;
    @FXML private TableColumn<Course, String> finalExam;
    @FXML private TableColumn<Course, String> location;
    @FXML private TableColumn<Course, String> teacherName;
    @FXML private ListView<String> facultyList;
    @FXML
    public void initialize() {
        // Bind columns to Student properties
        colmStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colmStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colmStudentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load student data initially
        loadStudentData();

        // Handle double-click for opening student details
        studentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click opens details
                Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    openStudentDetailsWindow(selectedStudent);
                }
            }
        });
        adminSubjects.setOnMouseClicked(mouseEvent ->  {
            String selectedItem = adminSubjects.getSelectionModel().getSelectedItem();
            if (selectedItem != null){
                deleteSubject(selectedItem);
            }
        });
    }

    private void openStudentDetailsWindow(Student student) {
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDetails.fxml"));
            Parent root = loader.load();

            // Get controller and pass student data
            StudentDetailsController controller = loader.getController();
            Stage detailsStage = new Stage();
            controller.setStudentDetails(student, detailsStage, this);

            // Setup stage
            detailsStage.setTitle("Student Details");
            detailsStage.setScene(new Scene(root));
            detailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentData() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();

        // Get students from Excel or Reading_Student (already loaded from Excel)
        Student[] students = Reading_Student.getAllStudents();
        if (students != null) {
            studentList.addAll(students);
        }

        // Set the table's items to the observable list
        studentTable.setItems(studentList);
    }


    public void refreshTable(Student student) throws IOException {
        Reading_Student.loadStudentCredentials();
        loadStudentData();
        studentTable.getItems().remove(student);
        studentTable.refresh();
    }

    public void loadAdminSubject() throws IOException{
        adminSubjects.getItems().clear(); // Clear existing items
        ReadingSubject.loadSubjects(); // Load subjects from Excel
        Subject[] subjects = ReadingSubject.getAllSubjects(); // Retrieve subjects

        if (subjects == null || subjects.length == 0) {
            adminSubjects.getItems().add("No subjects available.");
            return;
        }

        for (Subject subj : subjects) {
            if (subj != null) {
                adminSubjects.getItems().add(subj.getCode() + " - " + subj.getName()); // Display formatted subject info
            }
        }
    }
    public void deleteSubject(String subjectDelete) {
        deleteSubjectBtn.setOnAction(actionEvent -> {
            try {
                deleteSubject.deletingSubject(subjectDelete.replaceAll("\\s.*", ""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            adminSubjects.getItems().remove(subjectDelete);

        });
    }
    public void collapseMenu(){
        if(counter ==0) {
            tabs.setVisible(false);
            counter+=1;
            collapseButton.setText("Open Menu");

        }
        else{
            tabs.setVisible(true);
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
        for(Course course1: courses){
            if(!(course1.courseName.isEmpty())){
                courseList.add(course1);
            }
        }
        courseInfo.setItems(courseList);
    }
    @FXML
    private void faculty() throws IOException {
        facultyList.getItems().clear();
        ReadingFaculties.loadFaculties();
        Faculties[] faculties = ReadingFaculties.getAllFaculty();
        for (Faculties faculties1: faculties){
            if (faculties1 == null){
                continue;
            }
            if(!(faculties1.name.isEmpty())){
                facultyList.getItems().add("Name: " + faculties1.name + "         ID: " + faculties1.ID);
            }
        }
        facultyList.setOnMouseClicked(event -> {
            String selectedItem = facultyList.getSelectionModel().getSelectedItem();
            try {
                ReadingFaculties.loadFaculties();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("facultyDetails.fxml"));
                Parent root = loader.load();

                // Get controller and pass student data
                String name = selectedItem.replaceFirst(".*ID:", "").trim();
                facultyDetailsController controller = loader.getController();
                Stage detailsStage = new Stage();
                System.out.println(name);
                controller.setFacultyDetails(name);

                detailsStage.setTitle("Faculty Details");
                detailsStage.setScene(new Scene(root));
                detailsStage.show();
            }catch(IOException e){
                e.printStackTrace();
            }
        });
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
}