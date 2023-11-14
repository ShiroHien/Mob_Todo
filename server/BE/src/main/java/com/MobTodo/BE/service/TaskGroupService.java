package com.MobTodo.BE.service;

import com.MobTodo.BE.models.TaskGroup;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

public class TaskGroupService implements ITaskGroupService{
    private static final String COLLECTION_NAME = "TaskGroup";
    @Override
    public String createTaskGroup(TaskGroup data) throws ExecutionException, InterruptedException {
        if(postData( data, COLLECTION_NAME)) {
            return "Thêm nhóm task thành công";
        }
        return "Thêm nhóm task không thành công";
    }

    @Override
    public List<TaskGroup> getTaskGroupById(String userId) {
        return getListData(COLLECTION_NAME, "userId", userId, TaskGroup.class);
    }

    @Override
    public Boolean updateTaskGroup(TaskGroup data) {
        return updateData(COLLECTION_NAME, data.getId(), data);
    }

    @Override
    public Boolean deleteTaskGroup(String taskGroupId) {
        return null;
    }
}
