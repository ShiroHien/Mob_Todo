package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface ITimerService {
    Boolean createTimer(Timer data) throws ExecutionException, InterruptedException;
    List<Timer> getTimerById(String userId);
}
