package com.example.university_management;

public class Subject {
    String code;
    String name;
    public Subject(String code, String name){
        this.code = code;
        this.name = name;
    }
    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public String toString() {
        return code + " - " + name;
    }
}
