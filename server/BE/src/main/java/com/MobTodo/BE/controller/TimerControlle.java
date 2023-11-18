package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Timer;
import com.MobTodo.BE.service.ITimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/timer")
public class TimerControlle {
    @Autowired
    private ITimerService timerService;
    @PostMapping("/createTimer")
    public Boolean createTimer(@RequestBody Timer data) throws ExecutionException, InterruptedException {
        return timerService.createTimer(data);
    }
    @GetMapping("/getListTimer/{userId}")
    public List<Timer> getListTimer(@PathVariable String userId) {
        return timerService.getTimerById(userId);
    }
}
