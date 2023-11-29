package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.Timer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimerApi {
    @GET("timer/getListTimer/{userId}")
    Call<List<Timer>> getMyDayTask(@Path("userId") String userId);

}
