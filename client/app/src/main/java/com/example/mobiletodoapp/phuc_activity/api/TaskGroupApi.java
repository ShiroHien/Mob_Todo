package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskGroupApi {
    @POST("taskgroup/createTaskGroup")
    Call<Boolean> createTaskGroup(@Body TaskGroup data);

    @GET("taskgroup/getListGroup/{userId}")
    Call<List<TaskGroup>> getTasksGroup(@Path("userId") String userId);
}
