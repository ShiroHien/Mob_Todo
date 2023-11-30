package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Task;
import com.MobTodo.BE.models.TaskGroup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TaskGroupService implements ITaskGroupService {
    private static final String COLLECTION_NAME = "TaskGroup";

    @Override
    public Boolean createTaskGroup(TaskGroup data) throws ExecutionException, InterruptedException {
        String userId = data.getUserId();
        String title = data.getTitle();

        // Check if a task group with the same userId and title already exists
        boolean exists = checkExist(COLLECTION_NAME, "userId", userId, "title", title);

        if (exists) {
            // If exists, modify the title by appending a suffix
            int suffix = 1;
            while (exists) {
                String newTitle = title + " (" + suffix + ")";
                exists = checkExist(COLLECTION_NAME, "userId", userId, "title", newTitle);
                if (!exists) {
                    data.setTitle(newTitle);
                }
                suffix++;
            }
        }

        return postData(data, COLLECTION_NAME);
    }

    @Override
    public List<TaskGroup> getTaskGroupById(String userId) {
        return getListData(COLLECTION_NAME, "userId", userId, TaskGroup.class);
    }

    @Override
    public Boolean updateTaskGroup(TaskGroup data) {
        System.out.println("Id: " + data.getId());
        return updateData(COLLECTION_NAME, data.getId(), data);
    }

    @Override
    public Boolean deleteTaskGroup(String taskGroupId) {
        if (deleteData(COLLECTION_NAME, taskGroupId, "id") && deleteData("Task", taskGroupId, "TaskGroupId")) {
            return true;
        }
        return false;
    }

    @Override
    public TaskGroup getDetailTaskGroup(String taskgroupId) {
        return getDetail(COLLECTION_NAME, taskgroupId, TaskGroup.class);
    }
}
