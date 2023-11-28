package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.Task;
import com.MobTodo.BE.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    @PostMapping("/createTask")
    public Boolean createTask(@RequestBody Task data) throws ExecutionException, InterruptedException {
        System.out.println(data);
        return taskService.createTask(data);
    }

    @GetMapping("/getListTaskById/{taskGroupId}")
    public List<Task> getListTask(@PathVariable String taskGroupId) {
        return taskService.getTaskById(taskGroupId);
    }

    @GetMapping("/getTaskById/{taskId}")
    public Task getDetailTask(@PathVariable String taskId) {
        return taskService.getDetailTask(taskId);
    }

    @GetMapping("/getImportant/{userId}")
    public List<Task> getImportant(@PathVariable String userId) {
        return taskService.getImportant(userId);
    }

    @GetMapping("/getMyDay/{userId}")
    public List<Task> getMyDay(@PathVariable String userId) {
        return taskService.getMyDay(userId);
    }

    @PutMapping("/updateTask")
    public Boolean updateTask(@RequestBody Task data) {
        return taskService.updateTask(data);
    }

    @PutMapping("/setMyDay/{taskId}")
    public Boolean setMyDay(@PathVariable String taskId) {
        return taskService.setMyDay(taskId);
    }

    @PutMapping("/setImportant/{taskId}")
    public Boolean setImportant(@PathVariable String taskId) {
        return taskService.setImportant(taskId);
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public Boolean deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId);
    }

}
