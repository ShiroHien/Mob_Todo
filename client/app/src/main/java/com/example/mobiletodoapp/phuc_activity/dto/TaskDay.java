package com.example.mobiletodoapp.phuc_activity.dto;

public class TaskDay {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
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

    private String id;
    private String timetableId;
    private String title;
    private String description;

    public TaskDay(String timetableId, String title, String description, String startTime, String endTime) {
        this.id = "";
        this.timetableId = timetableId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String startTime;
    private String endTime;
}
