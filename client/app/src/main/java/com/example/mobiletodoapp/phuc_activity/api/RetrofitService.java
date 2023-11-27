package com.example.mobiletodoapp.phuc_activity.api;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Set connect timeout
                .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
                .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.111:8080/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
