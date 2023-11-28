package com.example.mobiletodoapp.phuc_activity.api;

import com.example.mobiletodoapp.phuc_activity.dto.Events;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventsApi {
    @POST("event/createEvent")
    Call<Boolean> createEvent(@Body Events data);

    @PUT("event/updateEvent")
    Call<Boolean> updateEvent(@Body Events data);

    @DELETE("event/deleteEvent/{timetableId}/{eventId}")
    Call<Boolean> deleteEvent(@Path("timetableId") String timetableId, @Path("eventId") String eventId);
}
