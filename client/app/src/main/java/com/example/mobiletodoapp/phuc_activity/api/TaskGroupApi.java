package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TaskGroupApi {
    @POST("taskgroup/createTaskGroup")
    Call<Boolean> createTaskGroup(@Body TaskGroup data);
}
