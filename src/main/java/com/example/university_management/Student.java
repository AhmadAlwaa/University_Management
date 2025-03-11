package com.example.university_management;

public class Student {
    String studentID;
    String password;
    String name;
    String address;
    String telephone;
    String email;
    String academicLevel;
    String currSem;
    String profilePhoto;
    String subjRej;
    String thesis;
    String progress;

    public Student(String studentID, String password, String name, String address, String telephone, String email, String academicLevel, String currSem, String profilePhoto, String subjRej, String thesis, String progress) {
        this.studentID = studentID;
        this.password = password;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.academicLevel = academicLevel;
        this.currSem = currSem;
        this.profilePhoto = profilePhoto;
        this.subjRej = subjRej;
        this.thesis = thesis;
        this.progress = progress;
    }
    public String getStudentID() {
        return studentID;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public String getCurrSem() {
        return currSem;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getSubjRej() {
        return subjRej;
    }

    public String getThesis() {
        return thesis;
    }

    public String getProgress() {
        return progress;
    }

}
