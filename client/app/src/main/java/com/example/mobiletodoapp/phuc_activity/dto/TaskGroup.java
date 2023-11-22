package com.example.mobiletodoapp.phuc_activity.dto;

public class TaskGroup {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TaskGroup( String title, String userId) {
        this.id = "";
        this.title = title;
        this.userId = userId;
    }

    private String id;
    private String title;
    private String userId;
}
