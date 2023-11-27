package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Events;
import com.MobTodo.BE.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/taskday")
public class EventController {
    @Autowired
    @Qualifier("eventService")
    private IEventService eventService;

    @PostMapping("/createTaskDay")
    public Boolean createTaskDay(@RequestBody Events data) throws ExecutionException, InterruptedException {
        return eventService.createTaskDay(data);
    }

    @GetMapping("/getDetailTaskDay/{timetableId}/{id}")
    public Events getDetailTaskDay(@PathVariable String timetableId,@PathVariable String id) {
        return eventService.getDetailTaskDay(timetableId, id);
    }

    @GetMapping("/getListTaskDay/{timetableId}")
    public List<Events> getListTaskDay(@PathVariable String timetableId) {
        return eventService.getListTaskDay(timetableId);
    }

    @PutMapping("/updateTaskDay")
    public Boolean updateTaskDay(@RequestBody Events data) {
        return eventService.updateTaskDay(data);
    }

    @DeleteMapping("/deleteTaskDay/{taskDayId}")
    public Boolean deleteTaskday(@PathVariable String taskDayId) {
        return eventService.deleteTaskDay(taskDayId);
    }
}
