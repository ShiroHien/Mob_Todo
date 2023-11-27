package com.example.mobiletodoapp.phuc_activity.dto;

import java.util.List;

public class Timetable {
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

    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }

    private String id;
    private String userId;
    private String dayTime;

    public Timetable(String userId, String dayTime, List<Events> events) {
        this.id = "";
        this.userId = userId;
        this.dayTime = dayTime;
        this.events = events;
    }

    private List<Events> events;
}
