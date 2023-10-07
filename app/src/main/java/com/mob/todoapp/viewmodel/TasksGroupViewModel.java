package com.mob.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mob.todoapp.model.TasksGroup;
import com.mob.todoapp.repository.TasksGroupDao;
import com.mob.todoapp.repository.TasksGroupRepository;

import java.util.List;

public class TasksGroupViewModel extends AndroidViewModel {
    private TasksGroupRepository repository;
    private LiveData<List<TasksGroup>> allTasksGroup;
    public TasksGroupViewModel(@NonNull Application application) {
        super(application);
        repository = new TasksGroupRepository(application);
        allTasksGroup = repository.getAllTasksGroup();
    }

    public void insert(TasksGroup tasksGroup) {
        repository.insert(tasksGroup);
    }

    public void update(TasksGroup tasksGroup) {
        repository.update(tasksGroup);
    }

    public void delete(TasksGroup tasksGroup) {
        repository.delete(tasksGroup);
    }

    public void deleteAll(TasksGroup tasksGroup) {
        repository.deleteAll();
    }

    public LiveData<List<TasksGroup>> getAllTasksGroup() {
        return allTasksGroup;
    }

}
