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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminDash implements Initializable {
    @FXML private TextField textEvent;
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
    @FXML private Text studentText;
    @FXML private Button deleteCourses;
    @FXML private Label capacityLabel;
    @FXML private Slider capacitySlider;
    @FXML private Label costLabel;
    @FXML private Slider costSlider;
    @FXML private DatePicker addEventDate;
    @FXML private TextField eventName;
    @FXML private TextField eventLocation;
    @FXML private Button addEventBtn;
    @FXML private TextField eventNameField;
    @FXML private TextField eventLocationField;
    @FXML private Slider timeSlider;
    @FXML private  Label timeLabel;
    @FXML private ChoiceBox<String> coursesOffered;
    @FXML private ChoiceBox<String> degree;
    @FXML private Button addFaculty;
    @FXML private TextField facultyName;
    @FXML private TextField researchIntereset;
    @FXML private TextField officeLocation;
    @FXML private  Button addSubject;
    @FXML private TextField subjectCodeField;
    @FXML private  TextField subjectName;
    @FXML private TextField addingCourses;
    @FXML private Label promptText;
    @FXML private Label studentsEnr;
    @FXML private ListView<String> studentEnrList;
    @FXML private ChoiceBox<String> studentSelect;
    @FXML private Button enrollStudent;
    @FXML private Label upcomingEvent;
    @FXML private Label upComingEventDate;
    static String name;
    static String address;
    static String telephone;
    static String email;
    private int counterText = 0;
    @FXML private Text eventText;
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
        addSubject.setOnAction(MouseEvent -> {
            Subject subject = new Subject(subjectCodeField.getText(), subjectName.getText());
            Subject.addSubject(subject);
            adminSubjects.getItems().add(subject.code + " - " + subject.name);
        });
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
        capacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            capacityLabel.setText("Capacity: " + Math.round(capacitySlider.getValue()));
        });
        costSlider.valueProperty().addListener((observable, oldValue, newValue) ->{
            costLabel.setText("Cost: " + String.format("%.2f", costSlider.getValue()));
        });
        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            timeLabel.setText(String.format("%.2f", timeSlider.getValue()));
        });
        addEventBtn.setStyle("-fx-background-color: "
                + "linear-gradient(#f49794, #ff6666), "
                + "radial-gradient(center 50% -40%, radius 200%, #ff7f7f 45%, #b30000 50%); "
                + "-fx-background-radius: 6, 5; "
                + "-fx-background-insets: 0, 1; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 5, 0.0, 0, 1); "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold;");
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
        year.setText(String.valueOf(dateFocus.getYear())); // Set year text
        month.setText(String.valueOf(dateFocus.getMonth())); // Set month text

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 2;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);
        Map<Integer, StackPane> dateStackPaneMap = new HashMap<>(); // Store stack panes for each date

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setArcWidth(30);
                rectangle.setArcHeight(30);
                rectangle.setFill(Color.rgb(255, 99, 71, 0.5));
                rectangle.setStroke(Color.BLACK);
                rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.LIGHTBLUE));
                rectangle.setOnMouseExited(event -> rectangle.setFill(Color.rgb(255, 99, 71, 0.5)));
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);

                rectangle.setOnMousePressed(event -> {
                    if (calculatedDate - dateOffset > 0 && calculatedDate - dateOffset <= dateFocus.getMonth().maxLength()) {
                        LocalDate selectedDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate - dateOffset);
                        addEventDate.setValue(selectedDate);
                    }
                });

                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setFill(Color.rgb(59, 69, 1, 0.95));
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        dateStackPaneMap.put(currentDate, stackPane); // Store stackPane for this date

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }

        addEventBtn.setOnAction(event -> {
            LocalDate selectedDate = addEventDate.getValue();
            String eventName = eventNameField.getText();
            String eventLocation = eventLocationField.getText();
            String time = timeLabel.getText();
            String eventCapacity = String.valueOf(Math.round(capacitySlider.getValue()));
            String eventCost = String.valueOf(costSlider.getValue());
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = time.replace(".", ":");
            LocalTime localTime = LocalTime.parse(formattedTime, timeFormatter);

            if (selectedDate != null && eventName != null && !eventName.isEmpty() && !eventLocation.isEmpty()) {
                ZonedDateTime eventDateTime = ZonedDateTime.of(selectedDate, localTime, ZoneId.systemDefault());
                eventNameField.clear();
                eventLocationField.clear();
                addEventDate.setValue(null);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
                String formatedDate = eventDateTime.format(formatter);
                Event.addEvent(eventName, "No Description",eventLocation, String.valueOf(formatedDate),eventCapacity, eventCost );
                StackPane selectedStackPane = dateStackPaneMap.get(selectedDate.getDayOfMonth());
                if (selectedStackPane != null) {
                    CalendarActivity activity = new CalendarActivity(eventDateTime, eventName, eventLocation);
                    List<CalendarActivity> calendarActivities = calendarActivityMap.computeIfAbsent(selectedDate.getDayOfMonth(), k -> new ArrayList<>());
                    calendarActivities.add(activity);

                    // Remove duplicate event title and use only createCalendarActivity
                    selectedStackPane.getChildren().removeIf(node -> node instanceof VBox);
                    createCalendarActivity(calendarActivities, (calendarHeight / 6) - strokeWidth - spacingV, (calendarWidth / 7) - strokeWidth - spacingH, selectedStackPane);
                }
            }
        });
    }
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        calendarActivityBox.setSpacing(2);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.9);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.8);
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            text.setWrappingWidth(rectangleWidth * 0.85); // Prevent text overflow
            text.setStyle("-fx-font-size: 12px; -fx-fill: black;"); // Make text visible

            int finalK = k;
            text.setOnMouseClicked(mouseEvent -> {
                System.out.println(text.getText());
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("eventDetails.fxml"));
                    Parent root = loader.load();
                    EventDetailsController controller = loader.getController();
                    Stage detailsStage = new Stage();
                    controller.setEventDetails(calendarActivities.get(finalK).getClientName(), this);

                    detailsStage.setTitle("Event Details");
                    detailsStage.setScene(new Scene(root));
                    detailsStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            text.setOnMouseEntered(mouseEvent -> text.setStyle("-fx-font-size: 12px; -fx-fill: #ae4802;"));
            text.setOnMouseExited(mouseEvent -> text.setStyle("-fx-font-size: 12px; -fx-fill: black;"));
            calendarActivityBox.getChildren().add(text);
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
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
        AtomicInteger count = new AtomicInteger(0); // Move outside so it persists across events

        Course course = new Course(null, null, null, null, null, null, null, null, null);
        promptText.setText("Enter Course Name");

        addingCourses.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode() == KeyCode.ENTER) {
                switch (count.getAndIncrement()) { // Get count value first, then increment
                    case 0:
                        course.courseName = addingCourses.getText();
                        promptText.setText("Enter Subject Code");
                        addingCourses.clear();
                        break;
                    case 1:
                        course.subjectCode = addingCourses.getText();
                        promptText.setText("Enter Section Number");
                        addingCourses.clear();
                        break;
                    case 2:
                        course.sectionNumber = "Section " + addingCourses.getText();
                        promptText.setText("Enter Capacity");
                        addingCourses.clear();
                        break;
                    case 3:
                        course.capacity = addingCourses.getText();
                        promptText.setText("Enter Lecture Time");
                        addingCourses.clear();
                        break;
                    case 4:
                        course.lectureTime = addingCourses.getText();
                        promptText.setText("Enter Final Exam Date");
                        addingCourses.clear();
                        break;
                    case 5:
                        course.finalExamDate = addingCourses.getText();
                        promptText.setText("Enter Location");
                        addingCourses.clear();
                        break;
                    case 6:
                        course.location = addingCourses.getText();
                        promptText.setText("Enter Teacher Name");
                        addingCourses.clear();
                        break;
                    case 7:
                        course.teacherName = addingCourses.getText();
                        promptText.setText("Enter Course Name");
                        addingCourses.clear();

                        // Find the next available section number
                        int i = 0;
                        for (Course course1 : courses) {
                            if (course1.courseCode == null) {
                                break;
                            }
                            i++;
                        }
                        course.courseCode = String.valueOf(i);

                        try {
                            Course.addCourse(course);
                            System.out.println("ADDING COURSE");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // Reset count after completion
                        count.set(0);
                        courseList.add(course);
                        addingCourses.deselect();
                        break;
                }
            }
        });
        String code;
        final Course[] selectedCourse = new Course[1];
        try {
            Reading_Student.loadStudentCredentials();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Student[] students = Reading_Student.getAllStudents();
        courseInfo.setItems(courseList);
        courseInfo.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && courseInfo.getSelectionModel().getSelectedItem() != null) { // Double-click opens details
                selectedCourse[0] = courseInfo.getSelectionModel().getSelectedItem();
                deleteCourses.setText("Delete "+ selectedCourse[0].courseName);

            }
            studentsEnr.setText("Students Enrolled In: " + courseInfo.getSelectionModel().getSelectedItem().courseName);
            studentEnrList.getItems().clear();
            for (Student student: students){
                if (student.subjRej.contains(courseInfo.getSelectionModel().getSelectedItem().subjectCode)){
                    studentEnrList.getItems().add(student.name);
                }
            }
        });
        studentSelect.getItems().clear();
        studentSelect.setValue("ENROLL STUDENT");
        for (Student student: students ) {
            if (student.name != null) studentSelect.getItems().add(student.name);
        }
        studentSelect.getItems().removeLast();
        enrollStudent.setOnAction(MouseEvent ->{
            if ( courseInfo.getSelectionModel().getSelectedItem()==null) {
                studentEnrList.getItems().clear();
                studentEnrList.getItems().add("No Course Selected");
            }else if(studentSelect.getValue().equals("ENROLL STUDENT")){
                studentEnrList.getItems().clear();
                studentEnrList.getItems().add("Select Student");
            }else if(studentEnrList.getItems().contains(studentSelect.getValue())){
                studentEnrList.getItems().clear();
                studentEnrList.getItems().add("Student Already Enrolled");
            }else {
                studentEnrList.getItems().add(studentSelect.getValue());
                try {
                    AddStudent.addEnrollment(studentSelect.getValue(),courseInfo.getSelectionModel().getSelectedItem().subjectCode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        deleteCourses.setOnAction(event -> {

            try {
                Course.deleteCourse(selectedCourse[0].courseName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            courseInfo.getItems().remove(selectedCourse[0]);
            deleteCourses.setText("Delete Course");
        });

    }
    @FXML
    public void faculty() throws IOException {
        facultyList.getItems().clear();
        ReadingFaculties.loadFaculties();
        Faculties[] faculties = ReadingFaculties.getAllFaculty();
        for (Faculties faculties1 : faculties) {
            if (faculties1 == null) {
                continue;
            }
            if (!(faculties1.name.isEmpty())) {
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
                controller.setFacultyDetails(name, this);

                detailsStage.setTitle("Faculty Details");
                detailsStage.setScene(new Scene(root));
                detailsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        coursesOffered.getItems().clear();
        coursesOffered.setValue("Select Course Offered");
        coursesOffered.getItems().add("Select Courses Offered");
        ReadingCourses.loadCourses();
        Course[] courses = ReadingCourses.getAllCourseInfo();
        Set<String> seenNames = new HashSet<>();
        degree.getItems().clear();
        degree.setValue("Select Degree");
        degree.getItems().add("Select Degree");
        degree.getItems().add("Ph.D");
        degree.getItems().add("Master's");
        for (Course course : courses) {
            if (seenNames.add(course.courseName)) {
                // If courseName is new, add to coursesOffered
                coursesOffered.getItems().add(course.courseName);
            }
        }

        coursesOffered.getItems().removeLast();
        addFaculty.setOnAction(MouseEvent ->{
            String newId = null;
            for (Faculties faculties1 : faculties) {
                if (faculties1 == null){
                    break;
                }
                int number = Integer.parseInt(faculties1.ID.substring(1));
                number++;
                newId = String.format("F%04d", number);
            }
            int lastSpace = facultyName.getText().lastIndexOf(" ");
            String lastName = facultyName.getText().substring(lastSpace+1);

            Faculties faculty = new Faculties(newId, facultyName.getText(), degree.getValue(), researchIntereset.getText(), lastName.toLowerCase() + "@university.edu", officeLocation.getText(), coursesOffered.getValue(), "default123");
            facultyList.getItems().add("Name: " + faculty.name + "         ID: " + faculty.ID);
            Faculties.addFaculty(faculty);
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
        CalendarActivity event = getUpcomingEvent();
        upcomingEvent.setText(String.valueOf(event.getClientName()));
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("h:mm M/d/yyyy");
        String formattedDate = event.getDate().format(displayFormatter);
        upComingEventDate.setText(formattedDate);


    }
    private void addEvent(){
        String academicLevel = "Undergraduate";
        String currSem = "Fall 2025";
        String profilePhoto = "default";
        String subjRej = "";
        String thesis = "";
        String progress = "0%";
        AddStudent student = new AddStudent("", "default123", name, address, telephone, email, academicLevel, currSem, profilePhoto, subjRej, thesis, progress);
        student.addStudent();
        int index = studentTable.getItems().size() -1;
        studentTable.getItems().add(index,student);
        studentTable.refresh();

    }
    @FXML
    private void clearText(){
        textEvent.setOnKeyPressed(keyEvent -> {
            if (!textEvent.getText().isEmpty()){
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    fields();
                    textEvent.clear();
                    counterText++;
                }
            }
                });
    }
    private void fields() {
        switch (counterText) {
            case 0:
                name = textEvent.getText();
                eventText.setText("Enter Address");
                studentText.setText("Name: " + name);
                break;
            case 1:
                address = textEvent.getText();
                eventText.setText("Enter Telephone");
                studentText.setText("Name: " + name +"  Address: " + address);
                break;
            case 2:
                telephone = textEvent.getText();
                eventText.setText("Enter Email");
                studentText.setText("Name: " + name +"  Address: " + address +"     Telephone: " + telephone);
                break;
            case 3:
                email = textEvent.getText();
                eventText.setText("Enter Name");
                studentText.setText("");
                addEvent();
                break;
        }
    }
    @FXML
    private void logOut() throws IOException {
        Stage stage = (Stage) deleteSubjectBtn.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(loginScreen.class.getResource("login.fxml")); //references the fxml called login
        Scene scene = new Scene(fxmlLoader.load(), 700, 500); //open login.fxml with dimensions 700 width and 500 height
        stage.setTitle("University Management System"); //sets the login screen title to university management system
        stage.setScene(scene);
        stage.show();

    }
    public void refreshEvents() throws IOException {
        calendar.getChildren().clear();
        drawCalendar();
    }
    private CalendarActivity getUpcomingEvent() throws IOException {
        ReadEvents.loadEvents(); // Load events
        Event[] events = ReadEvents.getAllEvents();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        CalendarActivity upcomingEvent = null;

        for (Event event : events) {
            if (event == null || event.dateAndTime == null || event.dateAndTime.trim().isEmpty()) {
                continue; // Skip invalid events
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            ZonedDateTime eventDateTime = LocalDateTime.parse(event.dateAndTime, formatter).atZone(ZoneId.systemDefault());

            if (eventDateTime.isAfter(now)) { // Event is in the future
                if (upcomingEvent == null || eventDateTime.isBefore(upcomingEvent.getDate())) {
                    upcomingEvent = new CalendarActivity(eventDateTime, event.eventName, event.location);
                }
            }
        }

        return upcomingEvent;
    }
}