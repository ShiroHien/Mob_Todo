package com.example.mobiletodoapp.phuc_activity.dto;

public class Task {
    private String id;
    private String taskGroupId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private boolean completed = false;
    private boolean myDay = false;
    private boolean important = false;

    public Task(String taskGroupId, String title, String description, String startTime, String endTime) {
        this.taskGroupId = taskGroupId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isMyDay() {
        return myDay;
    }

    public void setMyDay(boolean myDay) {
        this.myDay = myDay;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}
