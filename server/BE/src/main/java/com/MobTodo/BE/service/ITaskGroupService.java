package com.MobTodo.BE.service;

import com.MobTodo.BE.models.TaskGroup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Service
public interface ITaskGroupService {
    Boolean createTaskGroup(TaskGroup data) throws ExecutionException, InterruptedException;
    List<TaskGroup> getTaskGroupById(String userId);
    Boolean updateTaskGroup(TaskGroup data);
    Boolean deleteTaskGroup(String taskGroupId);
    TaskGroup getDetailTaskGroup(String TaskGroupId);
}
