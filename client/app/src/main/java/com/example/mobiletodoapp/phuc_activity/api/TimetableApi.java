package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.Timetable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TimetableApi {
    @POST("timetable/createTimetable")
    Call<Timetable> createTimetable(@Body Timetable data);

    @GET("timetable/getTimetableById/{userId}")
    Call<List<Timetable>> getTimetableById(@Path("userId") String userId);
}
