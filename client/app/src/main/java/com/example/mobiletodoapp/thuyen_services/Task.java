package com.example.mobiletodoapp.thuyen_services;

public class Task {
    static int increaseId = 0;
    private int id;
    private String title;
    private boolean type;
    boolean isCompleted;
    private String description;

    public static final boolean CHECK = true;
    public static final boolean UNCHECK = false;

    public Task() {
        increaseId ++;
        this.id = increaseId;
    }

    public Task(String title, boolean type, String description) {
        increaseId ++;
        this.id = increaseId;
        this.title = title;
        this.type = type;
        this.description = description;
    }
    public Task(String title, boolean type, String description, boolean isCompleted) {
        increaseId ++;
        this.id = increaseId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public static int getIncreaseId() {
        return increaseId;
    }

    public static void setIncreaseId(int increaseId) {
        Task.increaseId = increaseId;
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

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
