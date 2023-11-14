package com.MobTodo.BE.service;

import com.MobTodo.BE.models.User;

import java.util.concurrent.ExecutionException;

public interface IUserService {
    String logup(User user) throws ExecutionException, InterruptedException;
    User getUserDetail(String id) throws ExecutionException, InterruptedException;
}
