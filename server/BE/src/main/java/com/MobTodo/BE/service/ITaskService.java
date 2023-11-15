package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface ITaskService {
    String createTask(Task data) throws ExecutionException, InterruptedException;
    List<Task> getTaskById(String taskGroupId);
    Boolean updateTask(Task data);
    Boolean deleteTask(String taskId);
}