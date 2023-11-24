package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timetable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface ITimetableService {
    Timetable createTimetable(Timetable data) throws ExecutionException, InterruptedException;
    List<Timetable> getTimetableById(String userId);

}
