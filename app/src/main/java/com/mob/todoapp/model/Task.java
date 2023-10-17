package com.mob.todoapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "task")
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int tasksGroupId;
    private String title;
    private String timeExpired;
    private boolean isLoop;
    private boolean isAddedToMyDay;
    private boolean isRemind;
    private String note;

    public Task(int tasksGroupId, String title, String timeExpired, boolean isLoop, boolean isAddedToMyDay, boolean isRemind, String note) {
        this.tasksGroupId = tasksGroupId;
        this.title = title;
        this.timeExpired = timeExpired;
        this.isLoop = isLoop;
        this.isAddedToMyDay = isAddedToMyDay;
        this.isRemind = isRemind;
        this.note = note;
    }

    public Task() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTasksGroupId() {
        return tasksGroupId;
    }

    public void setTasksGroupId(int tasksGroupId) {
        this.tasksGroupId = tasksGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(String timeExpired) {
        this.timeExpired = timeExpired;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public boolean isAddedToMyDay() {
        return isAddedToMyDay;
    }

    public void setAddedToMyDay(boolean addedToMyDay) {
        isAddedToMyDay = addedToMyDay;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
