package com.example.mobiletodoapp.trung_activity;


import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobiletodoapp.phuc_activity.api.EventsApi;
import com.example.mobiletodoapp.phuc_activity.api.TimetableApi;
import com.example.mobiletodoapp.phuc_activity.dto.Events;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarUtils
{
    public static LocalDate selectedDate;

    public static String selectedTimetableId;
    public static TimetableApi timetableApi;

    public static EventsApi eventsApi;

    public static List<Timetable> existingTimetableList = new ArrayList<>();
    public static Boolean creatingTimetable = false;

    public static String monthDayYearDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter);
    }

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return  daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }


    public static CompletableFuture<Void> loadTimetableForUser(Context context, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        timetableApi.getTimetableById(userId).enqueue(new Callback<List<Timetable>>() {
            @Override
            public void onResponse(Call<List<Timetable>> call, Response<List<Timetable>> response) {
                try {
                    if (response.body() != null) {
                        existingTimetableList = response.body();
                    } else {
                        // Xử lý khi response body là null
                        showToast(context,"TaskDay List bị null");
                    }
                    future.complete(null);
                } catch (Exception e){
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<List<Timetable>> call, Throwable t) {
                showToast(context,"Không lấy đc timetable list");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ",t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public static CompletableFuture<Timetable> createTimetableForDate(Context context, Timetable timetable) {
        CompletableFuture<Timetable> future = new CompletableFuture<>();
        timetableApi.createTimetable(timetable).enqueue(new Callback<Timetable>() {
            @Override
            public void onResponse(Call<Timetable> call, Response<Timetable> response) {
                try {
                    if (response.body() != null) {
                        Log.d("Calendar","timetable response: "+response.body());
                        showToast(context,"tao timetable thanh cong");
                        CalendarUtils.selectedTimetableId = response.body().getId();
                        existingTimetableList.add(response.body());
                    } else {
                        // Xử lý khi response body là null
                        showToast(context,"timetable trả về bị null");
                    }
                    future.complete(null);
                } catch (Exception e){
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Timetable> call, Throwable t) {
                showToast(context,"Không lấy đc timetable list");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ",t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public static void handleTimetableForDate(LocalDate selectedDate, EventsAdapter eventsAdapter) {
        for(int i = 0; i< existingTimetableList.size(); i++){
            Timetable timetableItem = existingTimetableList.get(i);

            if(timetableItem.getDayTime().equals(CalendarUtils.monthDayYearDate(selectedDate))){
                selectedTimetableId = timetableItem.getId();
                eventsAdapter.updateEventsList(timetableItem.getEvents());
                return;
            }
        }
        selectedTimetableId = null;
        eventsAdapter.updateEventsList(new ArrayList<>());
    }

    // Static method to apply a fade-in animation to a View
    public static void fadeInAnimation(View view, int duration) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.start();
    }

    public static void scaleAnimation(View view) {
        Animation scaleAnimation = new ScaleAnimation(
                1.2f, 1.0f, // Start and end scale X
                1.2f, 1.0f, // Start and end scale Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X relative to the view width
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y relative to the view height
        );
        scaleAnimation.setDuration(300); // 300 milliseconds duration

        view.startAnimation(scaleAnimation);
    }
}