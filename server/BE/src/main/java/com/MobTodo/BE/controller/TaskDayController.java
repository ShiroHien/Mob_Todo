package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.TaskDay;
import com.MobTodo.BE.service.ITaskDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/taskday")
public class TaskDayController {
    @Autowired
    private ITaskDayService taskDayService;

    @PostMapping("/createTaskDay")
    public Boolean createTaskDay(@RequestBody TaskDay data) throws ExecutionException, InterruptedException {
        return taskDayService.createTaskDay(data);
    }

    @GetMapping("/getDetailTaskDay/{id}")
    public TaskDay getDetailTaskDay(@PathVariable String id) {
        return taskDayService.getDetailTaskDay(id);
    }

    @GetMapping("/getListTaskDay/{timetableId}")
    public List<TaskDay> getListTaskDay(@PathVariable String timetableId) {
        return taskDayService.getListTaskDay(timetableId);
    }

    @PutMapping("/updateTaskDay")
    public Boolean updateTaskDay(@RequestBody TaskDay data) {
        return taskDayService.updateTaskDay(data);
    }

    @DeleteMapping("/deleteTaskDay/{taskDayId}")
    public Boolean deleteTaskday(@PathVariable String taskDayId) {
        return taskDayService.deleteTaskDay(taskDayId);
    }
}
