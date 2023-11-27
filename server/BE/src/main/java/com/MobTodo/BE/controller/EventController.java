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
    public Boolean createEvent(@RequestBody Events data) throws ExecutionException, InterruptedException {
        return eventService.createEvent(data);
    }

    @GetMapping("/getDetailEvent/{timetableId}/{id}")
    public Events getDetailEvent(@PathVariable String timetableId, @PathVariable String id) {
        return eventService.getDetailEvent(timetableId, id);
    }

    @GetMapping("/getListEvent/{timetableId}")
    public List<Events> getListEvent(@PathVariable String timetableId) {
        return eventService.getListEvent(timetableId);
    }

    @PutMapping("/updateEvent")
    public Boolean updateEvent(@RequestBody Events data) {
        return eventService.updateEvent(data);
    }

    @DeleteMapping("/deleteEvent/{timetableId}/{eventId}")
    public Boolean deleteEvent(@PathVariable String timetableId, @PathVariable String eventId) {
        return eventService.deleteEvent(timetableId, eventId);
    }
}
