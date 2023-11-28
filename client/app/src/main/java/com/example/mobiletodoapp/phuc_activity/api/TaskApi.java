package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskApi {

    @GET("task/getListTaskById/{taskGroupId}")
    Call<List<Task>> getTasks(@Path("taskGroupId") String taskGroupId);

    @POST("task/createTask")
    Call<Boolean> createTask(@Body Task data);

    @GET("task/getImportant/{userId}")
    Call<List<Task>> getImportantTask(@Path("userId") String userId);

    @GET("task/getMyDay/{userId}")
    Call<List<Task>> getMyDayTask(@Path("userId") String userId);

    @GET("task/getTaskById/{taskId}")
    Call<Task> getDetailTask(@Path("taskId") String taskId);

    @PUT("task/updateTask")
    Call<Boolean> updateTask(@Body Task data);
}
