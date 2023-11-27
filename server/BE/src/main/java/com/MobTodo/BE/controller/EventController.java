package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Events;
import com.MobTodo.BE.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/taskday")
public class EventController {
    @Autowired
    private IEventService taskDayService;

    @PostMapping("/createTaskDay")
    public Boolean createTaskDay(@RequestBody Events data) throws ExecutionException, InterruptedException {
        return taskDayService.createTaskDay(data);
    }

    @GetMapping("/getDetailTaskDay/{id}")
    public Events getDetailTaskDay(@PathVariable String id) {
        return taskDayService.getDetailTaskDay(id);
    }

    @GetMapping("/getListTaskDay/{timetableId}")
    public List<Events> getListTaskDay(@PathVariable String timetableId) {
        return taskDayService.getListTaskDay(timetableId);
    }

    @PutMapping("/updateTaskDay")
    public Boolean updateTaskDay(@RequestBody Events data) {
        return taskDayService.updateTaskDay(data);
    }

    @DeleteMapping("/deleteTaskDay/{taskDayId}")
    public Boolean deleteTaskday(@PathVariable String taskDayId) {
        return taskDayService.deleteTaskDay(taskDayId);
    }
}
