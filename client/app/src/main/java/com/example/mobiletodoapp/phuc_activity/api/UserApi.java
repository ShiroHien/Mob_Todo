package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.model.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("user/loginUser")
    Call<Boolean> loginUser(@Body Login login);
}
