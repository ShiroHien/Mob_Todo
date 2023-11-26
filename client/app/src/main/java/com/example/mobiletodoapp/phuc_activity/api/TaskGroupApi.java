package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskGroupApi {
    @POST("taskgroup/createTaskGroup")
    Call<Boolean> createTaskGroup(@Body TaskGroup data);

    @GET("taskgroup/getListGroup/{userId}")
    Call<List<TaskGroup>> getTasksGroup(@Path("userId") String userId);

    @DELETE("taskgroup/deleteGroup/{id}")
    Call<Boolean> delateTaskGroup(@Path("id") String id);

    @PUT("taskgroup/updateGroup}")
    Call<Boolean> updateTaskGroup(@Body TaskGroup data);

    @GET("taskgroup/getTaskGroupById/{taskgroupId}")
    Call<TaskGroup> getTaskGroupById(@Path("taskgroupId") String taskGroupId);
}
