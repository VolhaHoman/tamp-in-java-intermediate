package com.epam.mentoring.taf.model;

public class UserDataModel {
    private String userName;
    private String userEmail;
    private String userPassword;

    public UserDataModel(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public UserDataModel() {
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
