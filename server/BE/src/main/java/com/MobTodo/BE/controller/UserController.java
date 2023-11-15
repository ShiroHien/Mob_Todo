package com.MobTodo.BE.controller;

import com.MobTodo.BE.models.User;
import com.MobTodo.BE.service.IUserService;
import com.MobTodo.BE.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/createUser")
    public String saveUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.logup(user);
    }
    @GetMapping("/getUserDetail/{id}")
    public User getUserDetail(@PathVariable String id) throws ExecutionException, InterruptedException {
        return userService.getUserDetail(id);
    }
}