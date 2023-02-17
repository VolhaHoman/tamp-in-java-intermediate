package com.epam.mentoring.taf.model;

public class User {
    public String email;
    public String password;
    public String username;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
