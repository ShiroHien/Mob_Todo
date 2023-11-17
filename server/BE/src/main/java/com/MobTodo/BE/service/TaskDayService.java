package com.MobTodo.BE.service;

import com.MobTodo.BE.models.TaskDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TaskDayService implements ITaskDayService {
    private static final String COLLECTION_NAME = "TaskDay";

    @Override
    public Boolean createTaskDay(TaskDay data) throws ExecutionException, InterruptedException {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            if (distanceTime(data.getStartTime(), data.getEndTime()) >= 0) {
                return postData(data, COLLECTION_NAME);
            }
        }
        return false;
    }

    @Override
    public TaskDay getDetailTaskDay(String id) {
        return getDetail(COLLECTION_NAME, id, TaskDay.class);
    }

    @Override
    public List<TaskDay> getListTaskDay(String timetableId) {
        return getListData(COLLECTION_NAME, "timetableId", timetableId, TaskDay.class);
    }

    @Override
    public Boolean updateTaskDay(TaskDay data) {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            return updateData(COLLECTION_NAME, data.getId(), data);
        }
        return false;
    }

    @Override
    public Boolean deleteTaskDay(String taskDayId) {
        return deleteData(COLLECTION_NAME, taskDayId, "id");
    }
}
