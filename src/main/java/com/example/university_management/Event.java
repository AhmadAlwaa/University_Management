package com.example.university_management;

public class Event {
    String eventCode;
    String eventName;
    String description;
    String location;
    String dateAndTime;
    String capacity;
    String cost;
    String image;
    String regStudents;
    public Event(String eventCode, String eventName, String description, String location, String dateAndTime, String capacity, String cost, String image, String regStudents){
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.dateAndTime = dateAndTime;
        this.capacity = capacity;
        this.cost = cost;
        this.image = image;
        this.regStudents = regStudents;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getCost() {
        return cost;
    }

    public String getImage() {
        return image;
    }

    public String getRegStudents() {
        return regStudents;
    }
    public void addEvent(){

    }
}

