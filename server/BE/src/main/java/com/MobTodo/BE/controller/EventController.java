package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Events;
import com.MobTodo.BE.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    @Qualifier("eventService")
    private IEventService eventService;

    @PostMapping("/createEvent")
    public Boolean createTaskDay(@RequestBody Events data) throws ExecutionException, InterruptedException {
        return eventService.createTaskDay(data);
    }

    @GetMapping("/getDetailEvent/{timetableId}/{id}")
    public Events getDetailTaskDay(@PathVariable String timetableId,@PathVariable String id) {
        return eventService.getDetailTaskDay(timetableId, id);
    }

    @GetMapping("/getListEvent/{timetableId}")
    public List<Events> getListTaskDay(@PathVariable String timetableId) {
        return eventService.getListTaskDay(timetableId);
    }

    @PutMapping("/updateEvent")
    public Boolean updateTaskDay(@RequestBody Events data) {
        return eventService.updateTaskDay(data);
    }

    @DeleteMapping("/deleteEvent/{timetableId}/{eventId}")
    public Boolean deleteTaskday(@PathVariable String timetableId, @PathVariable String eventId) {
        return eventService.deleteTaskDay(timetableId, eventId);
    }
}
