package com.MobTodo.BE.controller;

import com.MobTodo.BE.dto.Login;
import com.MobTodo.BE.dto.UpdateUser;
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
    @PostMapping("/loginUser")
    public User loginUser(@RequestBody Login user) {
        return userService.login(user);
    }
    @PostMapping("/createUser")
    public User saveUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        System.out.println(user);
        return userService.logup(user);
    }
    @GetMapping("/getUserDetail/{id}")
    public User getUserDetail(@PathVariable String id) throws ExecutionException, InterruptedException {
        return userService.getUserDetail(id);
    }
    @PutMapping("/updateUser/{userId}")
    public Boolean updateUser(@RequestBody UpdateUser data, @PathVariable String userId) {
        return userService.updateUser(data, userId);
    }
}
