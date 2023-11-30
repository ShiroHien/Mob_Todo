package com.example.mobiletodoapp.phuc_activity.dto;

public class UpdateUser {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateUser(String username, String name) {
        this.username = username;
        this.name = name;
    }

    private String username;
    private String name;
}
