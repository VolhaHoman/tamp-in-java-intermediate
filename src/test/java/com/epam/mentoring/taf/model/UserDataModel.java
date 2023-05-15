package com.epam.mentoring.taf.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userName", "userEmail", "userPassword", "celebUsername"})
public class UserDataModel {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String celebUsername;

    public UserDataModel(String userName, String userEmail, String userPassword, String celebUsername) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.celebUsername = celebUsername;
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

    public String getCelebUsername() {
        return celebUsername;
    }
}
