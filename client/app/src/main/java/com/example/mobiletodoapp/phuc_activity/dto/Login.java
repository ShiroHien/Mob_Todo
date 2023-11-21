package com.example.mobiletodoapp.phuc_activity.dto;

public class Login {
    public Login(String toString, String toString1) {
        this.email = toString;
        this.password = toString1;
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

    private String email;
    private String password;
}
