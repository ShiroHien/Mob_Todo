package com.mob.todoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mob.todoapp.model.TasksGroup;

import java.util.List;

@Dao
public interface TasksGroupDao {
    @Insert
    void insert(TasksGroup tasksGroup);
    @Update
    void update(TasksGroup tasksGroup);
    @Delete
    void delete(TasksGroup tasksGroup);
    @Query("select * from tasksgroup")
    LiveData<List<TasksGroup>> getAllTasksGroup();

    @Query("select * from tasksgroup where id = (:id)")
    TasksGroup getTasksGroupById(int id);

    @Query(("delete from tasksgroup"))
    void deleteAll();
}
