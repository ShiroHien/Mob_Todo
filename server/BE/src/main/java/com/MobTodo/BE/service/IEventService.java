package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Events;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface IEventService {
    Boolean createEvent(Events data) throws ExecutionException, InterruptedException;
    Events getDetailEvent(String timetableId, String id);
    List<Events> getListEvent(String timetableId);
    Boolean updateEvent(Events data);
    Boolean deleteEvent(String timetableId, String taskDayId);
}
