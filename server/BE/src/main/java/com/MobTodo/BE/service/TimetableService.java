package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timetable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class TimetableService implements ITimetableService {
    private static final String COLLECTION_NAME = "Timetable";

    @Override
    public Timetable createTimetable(Timetable data) throws ExecutionException, InterruptedException {
        if (!checkExist(COLLECTION_NAME, "dayTime", data.getDayTime(), "userId", data.getUserId())) {
            if (checkDateFormat(data.getDayTime())) {
                if (postData(data, COLLECTION_NAME)) {
                    return getDetailByFieldName(COLLECTION_NAME, "dayTime", data.getDayTime(), "userId", data.getUserId(), Timetable.class);
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Timetable> getTimetableById(String userId) {
        return getListData(COLLECTION_NAME, "userId", userId, Timetable.class);
    }
}
