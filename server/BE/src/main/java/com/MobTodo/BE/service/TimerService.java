package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
            if (checkDateFormat(data.getDay())) {
                Timer existingTimer = getDetail(COLLECTION_NAME, data.getId(), Timer.class);
                if (existingTimer != null) {
                    double newDuringTime = existingTimer.getDuringTime() + data.getDuringTime();
                    data.setDuringTime(newDuringTime);
                }
                return updateData(COLLECTION_NAME, data.getId(), data);
            }
        }
        return false;
    }

    @Override
    public List<Timer> getTimerById(String userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysAgo = currentDate.minusDays(6);

        List<Timer> timersInRange = getListDataInRange(COLLECTION_NAME, "userId", userId, Timer.class, sevenDaysAgo, currentDate);

        List<Timer> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = currentDate.minusDays(i);
            Timer timerOfDay = findTimerByDay(timersInRange, date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            if (timerOfDay != null) {
                result.add(timerOfDay);
            } else {
                Timer emptyTimer = new Timer();
                emptyTimer.setDuringTime(0);
                emptyTimer.setDay(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                result.add(emptyTimer);
            }
        }
        Collections.reverse(result);

        return result;
    }

    private Timer findTimerByDay(List<Timer> timers, String day) {
        return timers.stream()
                .filter(timer -> timer.getDay().equals(day))
                .findFirst()
                .orElse(null);
    }

    private boolean isDateInRange(String date, LocalDate startDate, LocalDate endDate) {
        LocalDate timerDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return !timerDate.isBefore(startDate) && !timerDate.isAfter(endDate);
    }

    private List<Timer> getListDataInRange(String collectionName, String fieldName, String fieldValue, Class<Timer> type, LocalDate startDate, LocalDate endDate) {
        return getListData(collectionName, fieldName, fieldValue, type)
                .stream()
                .filter(timer -> isDateInRange(timer.getDay(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
