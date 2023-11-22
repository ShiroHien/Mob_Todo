package com.example.mobiletodoapp.thuyen_services;

public class TasksGroup {
    static int increaseId = 0;
    int id;
    String title;

    public TasksGroup(String title) {
        increaseId++;
        this.id = increaseId;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
