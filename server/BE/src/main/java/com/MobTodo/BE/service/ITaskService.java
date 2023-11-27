package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface ITaskService {
    Boolean createTask(Task data) throws ExecutionException, InterruptedException;
    List<Task> getTaskById(String taskGroupId);
    Task getDetailTask(String taskId);
    Boolean updateTask(Task data);
    Boolean deleteTask(String taskId);
    List<Task> getImportant(String taskgroupId);
    List<Task> getMyDay(String taskgroupId);
}
