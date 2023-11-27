package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Events;
import com.MobTodo.BE.models.Timetable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.MobTodo.BE.Reusable.Function.*;

@Service
@Primary
public class EventService implements IEventService {
    private static final String COLLECTION_NAME = "Timetable";

    @Override
    public Boolean createTaskDay(Events data) throws ExecutionException, InterruptedException {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            if (distanceTime(data.getStartTime(), data.getEndTime()) >= 0) {
                Timetable timetable = getDetail(COLLECTION_NAME, data.getTimetableId(), Timetable.class);
                if (timetable != null) {
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
    public Events getDetailTaskDay(String timetableId, String id) {
        Timetable timetable = getDetail(COLLECTION_NAME, timetableId, Timetable.class);
        if (timetable != null) {
            for (Events event : timetable.getEvents()) {
                if (event.getId().equals(id)) {
                    return event;
                }
            }
        }
        return null;
    }

    @Override
    public List<Events> getListTaskDay(String timetableId) {
        Timetable timetable = getDetail(COLLECTION_NAME, timetableId, Timetable.class);
        if (timetable != null) {
            return timetable.getEvents();
        }
        return null;
    }

    @Override
    public Boolean updateTaskDay(Events data) {
        if (checkTimeFormat(data.getStartTime()) && checkTimeFormat(data.getEndTime())) {
            if (distanceTime(data.getStartTime(), data.getEndTime()) >= 0) {
                Timetable timetable = getDetail(COLLECTION_NAME, data.getTimetableId(), Timetable.class);
                if (timetable != null) {
                    List<Events> eventsList = timetable.getEvents();
                    for (int i = 0; i < eventsList.size(); i++) {
                        Events existingEvent = eventsList.get(i);
                        if (existingEvent.getId().equals(data.getId())) {
                            eventsList.set(i, data);
                            return updateData(COLLECTION_NAME, timetable.getId(), timetable);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean deleteTaskDay(String timetableId, String eventId) {
        Timetable timetable = getDetail(COLLECTION_NAME, timetableId, Timetable.class);
        if (timetable != null) {
            List<Events> eventsList = timetable.getEvents();
            for (Events event : eventsList) {
                if (event.getId().equals(eventId)) {
                    eventsList.remove(event);
                    return updateData(COLLECTION_NAME, timetable.getId(), timetable);
                }
            }
        }
        return false;
    }

}
