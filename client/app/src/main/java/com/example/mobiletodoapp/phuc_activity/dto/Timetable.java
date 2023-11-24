package com.example.mobiletodoapp.phuc_activity.dto;

public class Timetable {
    private String id;

    public Timetable(String userId, String dayTime) {
        this.id = "";
        this.userId = userId;
        this.dayTime = dayTime;
    }

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    private String dayTime;
}
