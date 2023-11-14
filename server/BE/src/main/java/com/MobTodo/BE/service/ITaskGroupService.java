package com.MobTodo.BE.service;

import com.MobTodo.BE.models.TaskGroup;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ITaskGroupService {
    String createTaskGroup(TaskGroup data) throws ExecutionException, InterruptedException;
    List<TaskGroup> getTaskGroupById(String userId);
    Boolean updateTaskGroup(TaskGroup data);
    Boolean deleteTaskGroup(String taskGroupId);
}
