package com.MobTodo.BE.service;

import com.MobTodo.BE.dto.Login;
import com.MobTodo.BE.models.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
@Service
public interface IUserService {
    Boolean logup(User user) throws ExecutionException, InterruptedException;
    User getUserDetail(String id) throws ExecutionException, InterruptedException;
    Boolean login(Login user);
}
