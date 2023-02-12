package com.epam.mentoring.taf.data;

public class UserDataDTO {

    private final String userName;
    private final String userEmail;
    private final String userPassword;

    public UserDataDTO(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
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
