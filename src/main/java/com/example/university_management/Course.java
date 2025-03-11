package com.example.university_management;

public class Course {
    String courseCode;
    String courseName;
    String subjectCode;
    String sectionNumber;
    String capacity;
    String lectureTime;
    String finalExamDate;
    String location;
    String teacherName;
    public Course(String courseCode, String courseName, String subjectCode, String sectionNumber, String capacity, String lectureTime, String finalExamDate, String location, String teacherName){
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.subjectCode = subjectCode;
        this.sectionNumber = sectionNumber;
        this. capacity = capacity;
        this.lectureTime = lectureTime;
        this.finalExamDate = finalExamDate;
        this.location = location;
        this.teacherName = teacherName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public String getFinalExamDate() {
        return finalExamDate;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
