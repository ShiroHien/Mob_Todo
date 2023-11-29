package com.example.mobiletodoapp.phuc_activity.dto;

public class Timer {
    private String id;
    private String userId;
    private int duringTime;
    private String day;

    public Timer(String userId, int duringTime, String day) {
        this.userId = userId;
        this.duringTime = duringTime;
        this.day = day;
    }

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

    public int getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(int duringTime) {
        this.duringTime = duringTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
