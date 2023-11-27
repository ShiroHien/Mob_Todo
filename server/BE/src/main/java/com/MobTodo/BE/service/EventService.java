package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Events;
import com.MobTodo.BE.models.Timetable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
public class EventService implements IEventService {
    private static final String COLLECTION_NAME = "Timetable";

    @Override
    public Boolean createTaskDay(Events data) throws ExecutionException, InterruptedException {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            if (distanceTime(data.getStartTime(), data.getEndTime()) >= 0) {
                Timetable timetable = getDetail(COLLECTION_NAME, data.getTimetableId(), Timetable.class);
                if(timetable != null) {
                    String randomId = generateRandomId(COLLECTION_NAME);
                    data.setId(randomId);
                    timetable.getEvents().add(data);
                    return updateData(COLLECTION_NAME, timetable.getId(), timetable);
                }
            }
        }
        return false;
    }

    @Override
    public Events getDetailTaskDay(String id) {
        return getDetail(COLLECTION_NAME, id, Events.class);
    }

    @Override
    public List<Events> getListTaskDay(String timetableId) {
        return getListData(COLLECTION_NAME, "id", timetableId, Events.class);
    }

    @Override
    public Boolean updateTaskDay(Events data) {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            return updateData(COLLECTION_NAME, data.getId(), data);
        }
        return false;
    }

    @Override
    public Boolean deleteTaskDay(String taskDayId) {
        return deleteData(COLLECTION_NAME, taskDayId, "id");
    }
}
