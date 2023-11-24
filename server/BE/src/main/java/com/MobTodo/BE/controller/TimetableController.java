package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Timetable;
import com.MobTodo.BE.service.ITimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/timetable")
public class TimetableController {
    @Autowired
    private ITimetableService timetableService;
    @PostMapping("/createTimetable")
    public Timetable createTimetable(@RequestBody Timetable data) throws ExecutionException, InterruptedException {
        return timetableService.createTimetable(data);
    }
    @GetMapping("/getListTimetable/{userId}")
    public List<Timetable> getListTimetable(@PathVariable String userId) {
        return timetableService.getTimetableById(userId);
    }
}
