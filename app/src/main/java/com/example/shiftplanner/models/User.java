package com.example.shiftplanner.models;

public class User {

    private String email;
    private String password;
    private String employeeID;

    public User(){

    }

    public User(String email, String password, String employeeID) {
        this.email = email;
        this.password = password;
        this.employeeID = employeeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
}
