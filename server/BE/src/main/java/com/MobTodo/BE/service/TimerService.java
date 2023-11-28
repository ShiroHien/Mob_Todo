package com.MobTodo.BE.service;

import com.MobTodo.BE.models.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        LocalDate sevenDaysAgo = currentDate.minusDays(7);
        List<Timer> timersInRange = getListDataInRange(COLLECTION_NAME, "userId", userId, Timer.class, sevenDaysAgo, currentDate);
        return timersInRange.stream()
                .filter(timer -> isDateInRange(timer.getDay(), sevenDaysAgo, currentDate))
                .collect(Collectors.toList());
    }

    private boolean isDateInRange(String date, LocalDate startDate, LocalDate endDate) {
        LocalDate timerDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return !timerDate.isBefore(startDate) && !timerDate.isAfter(endDate);
    }

    // Thêm phương thức getListDataInRange để lấy danh sách dữ liệu trong khoảng thời gian
    private List<Timer> getListDataInRange(String collectionName, String fieldName, String fieldValue, Class<Timer> type, LocalDate startDate, LocalDate endDate) {
        // Thực hiện truy vấn dữ liệu trong khoảng thời gian từ startDate đến endDate
        // Sử dụng hàm getListDataInRange có sẵn
        return getListData(collectionName, fieldName, fieldValue, type)
                .stream()
                .filter(timer -> isDateInRange(timer.getDay(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
