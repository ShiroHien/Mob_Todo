package com.mob.todoapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TasksGroup {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    public TasksGroup(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
