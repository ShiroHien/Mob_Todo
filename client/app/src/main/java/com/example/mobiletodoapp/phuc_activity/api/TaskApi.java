package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskApi {

    @GET("task/getListTaskById/{taskGroupId}")
    Call<List<Task>> getTasks(@Path("taskGroupId") String taskGroupId);

}
