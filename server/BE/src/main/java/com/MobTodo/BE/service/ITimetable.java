package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timetable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITimetable {
    Boolean createTimetable(Timetable data);
    List<Timetable> getTimetableById(String userId);

}
