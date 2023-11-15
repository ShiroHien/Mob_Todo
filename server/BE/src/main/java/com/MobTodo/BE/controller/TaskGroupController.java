package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.TaskGroup;
import com.MobTodo.BE.service.ITaskGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/taskgroup")
public class TaskGroupController {
    @Autowired
    private ITaskGroupService taskGroupService;
    @PostMapping("/createTaskGroup")
    public String createGroup(@RequestBody TaskGroup data) throws ExecutionException, InterruptedException {
        return taskGroupService.createTaskGroup(data);
    }
    @GetMapping("/getListGroup/{userId}")
    public List<TaskGroup> getListGroup(@PathVariable String userId) {
        return taskGroupService.getTaskGroupById(userId);
    }
    @PutMapping("/updateGroup")
    public Boolean updateTaskGroup(@RequestBody TaskGroup data) {
        return taskGroupService.updateTaskGroup(data);
    }
    @DeleteMapping("/deleteGroup/{id}")
    public Boolean deleteGroup(@PathVariable String id) {
        return taskGroupService.deleteTaskGroup(id);
    }
}
