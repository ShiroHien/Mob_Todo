package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TaskService implements ITaskService {
    private static final String COLLECTION_NAME = "Task";

    @Override
    public Boolean createTask(Task data) throws ExecutionException, InterruptedException {
        System.out.println(distanceTime(data.getStartTime(), data.getEndTime()));
        if (distanceTime(data.getStartTime(), data.getEndTime()) >= 0) {
            return postData(data, COLLECTION_NAME);
        }
        return false;
    }

    @Override
    public List<Task> getTaskById(String taskGroupId) {
        return getListData(COLLECTION_NAME, "taskGroupId", taskGroupId, Task.class);
    }

    @Override
    public Boolean updateTask(Task data) {
        return updateData(COLLECTION_NAME, data.getId(), data);
    }

    @Override
    public Boolean deleteTask(String taskId) {
        if (deleteData(COLLECTION_NAME, taskId, "id")) {
            return true;
        }
        return false;
    }
}
