package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TaskService implements ITaskService {
    private static final String COLLECTION_NAME = "Task";

    @Override
    public Boolean createTask(Task data) throws ExecutionException, InterruptedException {
        if (checkDateTimeFormat(data.getStartTime()) && checkDateTimeFormat(data.getEndTime())) {
            if (distanceDateTime(data.getStartTime(), data.getEndTime()) >= 0) {
                return postData(data, COLLECTION_NAME);
            }
        }
        return false;
    }

    @Override
    public List<Task> getTaskById(String taskGroupId) {
        return getListData(COLLECTION_NAME, "taskGroupId", taskGroupId, Task.class);
    }

    @Override
    public Task getDetailTask(String taskId) {
        return getDetail(COLLECTION_NAME, taskId, Task.class);
    }

    @Override
    public Boolean updateTask(Task data) {
        if (checkDateTimeFormat(data.getStartTime()) && checkDateTimeFormat(data.getEndTime())) {
            return updateData(COLLECTION_NAME, data.getId(), data);
        }
        return false;
    }

    @Override
    public Boolean deleteTask(String taskId) {
        if (deleteData(COLLECTION_NAME, taskId, "id")) {
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getImportant(String taskgroupId) {
        return getListDataByFieldName(COLLECTION_NAME, "taskGroupId", taskgroupId, "important", true, Task.class);
    }

    @Override
    public List<Task> getMyDay(String taskgroupId) {
        return getListDataByFieldName(COLLECTION_NAME, "taskGroupId", taskgroupId, "myDay", true, Task.class);
    }
}
