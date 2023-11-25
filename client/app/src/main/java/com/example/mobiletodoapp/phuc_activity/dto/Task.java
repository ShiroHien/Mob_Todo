package com.example.mobiletodoapp.phuc_activity.dto;

public class Task {
    private String id;
    private String taskGroupId;
    private String description;
    //0 là chưa hoàn thành, 1 là đã hoàn thành
    private int isCompleted = 0;
    private String startTime;
    private String endTime;
    // 0 là bình thường, 1 là MyDay, 2 là Important
    private boolean isMyDay;
    private boolean isImportant;

    public Task(String taskGroupId, String description, String startTime, String endTime) {
        this.taskGroupId = taskGroupId;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
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

    public boolean isMyDay() {
        return isMyDay;
    }

    public void setMyDay(boolean myDay) {
        isMyDay = myDay;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }
}
