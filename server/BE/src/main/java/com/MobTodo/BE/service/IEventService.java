package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Events;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface IEventService {
    Boolean createTaskDay(Events data) throws ExecutionException, InterruptedException;
    Events getDetailTaskDay(String timetableId, String id);
    List<Events> getListTaskDay(String timetableId);
    Boolean updateTaskDay(Events data);
    Boolean deleteTaskDay(String timetableId, String taskDayId);
}
