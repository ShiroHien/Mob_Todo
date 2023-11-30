package com.MobTodo.BE.service;

import com.MobTodo.BE.dto.Login;
import com.MobTodo.BE.dto.UpdateUser;
import com.MobTodo.BE.models.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
@Service
public interface IUserService {
    User logup(User user) throws ExecutionException, InterruptedException;
    User getUserDetail(String id) throws ExecutionException, InterruptedException;
    User login(Login user);
    Boolean updateUser(UpdateUser data, String userId);
}
