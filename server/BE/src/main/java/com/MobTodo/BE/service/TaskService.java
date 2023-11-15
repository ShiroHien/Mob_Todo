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
    public String createTask(Task data) throws ExecutionException, InterruptedException {
        if (postData(data, COLLECTION_NAME)) {
            return "Thêm task thành công";
        }
        return "Thêm task không thành công";
    }

    @Override
    public List<Task> getTaskById(String taskGroupId) {
        return getListData(COLLECTION_NAME, "TaskGroupId", taskGroupId, Task.class);
    }

    @Override
    public Boolean updateTask(Task data) {
        return updateData(COLLECTION_NAME, data.getId(), data);
    }

    @Override
    public Boolean deleteTask(String taskId) {
        return null;
    }
}
