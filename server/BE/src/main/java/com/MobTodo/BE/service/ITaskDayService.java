package com.MobTodo.BE.service;

import com.MobTodo.BE.models.TaskDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface ITaskDayService {
    Boolean createTaskDay(TaskDay data) throws ExecutionException, InterruptedException;
    TaskDay getDetailTaskDay(String id);
    List<TaskDay> getListTaskDay(String timetableId);
    Boolean updateTaskDay(TaskDay data);
    Boolean deleteTaskDay(String taskDayId);
}
