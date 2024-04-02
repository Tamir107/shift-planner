package com.example.shiftplanner.models;

public class Shift {

    private String firstName, lastName, employeeID, date, hoursDescription;
    private int hoursID;

    public Shift(String firstName, String lastName, String employeeID, String date, String hoursDescription, int hoursID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeID = employeeID;
        this.date = date;
        this.hoursDescription = hoursDescription;
        this.hoursID = hoursID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHoursDescription() {
        return hoursDescription;
    }

    public void setHoursDescription(String hoursDescription) {
        this.hoursDescription = hoursDescription;
    }

    public int getHoursID() {
        return hoursID;
    }

    public void setHoursID(int hoursID) {
        this.hoursID = hoursID;
    }
}
