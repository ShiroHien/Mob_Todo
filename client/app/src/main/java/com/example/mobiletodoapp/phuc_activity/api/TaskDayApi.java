package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.TaskDay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskDayApi {
    @POST("taskday/createTaskDay")
    Call<Boolean> createTaskDay(@Body TaskDay data);

    @GET("taskday/getDetailTaskDay/{id}")
    Call<List<TaskDay>> getDetailTaskDay(@Path("id") String id);

    @GET("taskday/getListTaskDay/{timetableId}" )
    Call<List<TaskDay>> getListTaskDay(@Path("timetableId") String timetableId);

    @PUT("taskday/updateTaskDay")
    Call<Boolean> updateTaskDay(@Body TaskDay data);

    @DELETE("taskday/deleteTaskDay/{taskDayId")
    Call<Boolean> deleteTaskday(@Path("taskDayId") String taskDayId);
}
