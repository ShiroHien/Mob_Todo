package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import com.MobTodo.BE.models.TaskGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TaskService implements ITaskService {
    private static final String COLLECTION_NAME = "Task";
    private static final String COLLECTION_NAME_TASK_GROUP = "TaskGroup";

    @Override
    public Boolean createTask(Task data) throws ExecutionException, InterruptedException {
        if (checkDateTimeFormat(data.getStartTime()) && checkDateTimeFormat(data.getEndTime())) {
            if (distanceDateTime(data.getStartTime(), data.getEndTime()) >= 0) {
                System.out.println("Passed 1");
                return postData(data, COLLECTION_NAME);
            }
        }
        System.out.println("Passed 2");
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
    public List<Task> getImportant(String userId) {
        List<Task> importantTasks = new ArrayList<>();
        List<TaskGroup> taskGroups = getListData(COLLECTION_NAME_TASK_GROUP, "userId", userId, TaskGroup.class);
        for (TaskGroup taskGroup : taskGroups) {
            importantTasks.addAll(getListDataByFieldName(
                    COLLECTION_NAME,
                    "taskGroupId",
                    taskGroup.getId(),
                    "important",
                    true,
                    Task.class
            ));
        }

        return importantTasks;
    }

    @Override
    public List<Task> getMyDay(String userId) {
        List<Task> myDayTasks = new ArrayList<>();

        // Get all TaskGroups for the given userId
        List<TaskGroup> taskGroups = getListData(COLLECTION_NAME_TASK_GROUP, "userId", userId, TaskGroup.class);

        // For each TaskGroup, get tasks with myDay set to true
        for (TaskGroup taskGroup : taskGroups) {
            myDayTasks.addAll(getListDataByFieldName(
                    COLLECTION_NAME,
                    "taskGroupId",
                    taskGroup.getId(),
                    "myDay",
                    true,
                    Task.class
            ));
        }

        return myDayTasks;
    }
}
