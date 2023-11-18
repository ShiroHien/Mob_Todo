package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;
@Service
public class TimerService implements ITimerService {
    private static final String COLLECTION_NAME = "Timer";

    @Override
    public Boolean createTimer(Timer data) throws ExecutionException, InterruptedException {
        if (!checkExist(COLLECTION_NAME, "day", data.getDay())) {
            if (checkDateFormat(data.getDay())) {
                return postData(data, COLLECTION_NAME);
            }
        } else {
            if(checkDateFormat(data.getDay())) {
                return updateData(COLLECTION_NAME, data.getId(), data);
            }
        }
        return false;
    }

    @Override
    public List<Timer> getTimerById(String userId) {
        return getListData(COLLECTION_NAME, "userId", userId, Timer.class);
    }
}
