package com.example.mobiletodoapp.trung_activity;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.eventsApi;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.databinding.ActivityEditEventBinding;
import com.example.mobiletodoapp.phuc_activity.dto.Events;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventActivity extends AppCompatActivity {
    String id,timetableId,startTime,endTime,title,description;
    int startHour,startMin,endHour,endMin;
    private ActivityEditEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setEventData();
        initWidgets();

    }

    private void initWidgets() {
        hideLoading();
        binding.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        startHour = h;
                        startMin = m;
                        binding.etStartTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,startHour,startMin,true);
                timePickerDialog.setTitle("Chọn giở bắt đầu");
                timePickerDialog.show();
            }
        });
        binding.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        endHour = h;
                        endMin = m;
                        binding.etEndTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,endHour,endMin,true);
                timePickerDialog.setTitle("Chọn giở kết thúc");
                timePickerDialog.show();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Events event = new Events(timetableId,binding.etTitle.getText().toString(),binding.etDescription.getText().toString(),
                        binding.etStartTime.getText().toString(),binding.etEndTime.getText().toString());
                event.setId(id);
                Log.d("Calendar","event: "+event.getTitle());
                showLoading();
                editEvent(event).whenComplete((result,throwable)->{
                    hideLoading();
                    onBackPressed();
                });
            }
        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showLoading();
                deleteEvent(timetableId,id).whenComplete((result,throwable)->{
//                    hideLoading();
                });

                onBackPressed();
            }
        });
    }


    private void setEventData() {
        id = this.getIntent().getStringExtra("id");
        timetableId = this.getIntent().getStringExtra("timetableId");
        startTime = this.getIntent().getStringExtra("startTime");
        endTime = this.getIntent().getStringExtra("endTime");
        title = this.getIntent().getStringExtra("title");
        description = this.getIntent().getStringExtra("description");

        binding.etTitle.setText(title);
        binding.etStartTime.setText(startTime);
        binding.etEndTime.setText(endTime);
        binding.etDescription.setText(description);
    }

    public CompletableFuture<Void> editEvent(Events newEvent) {
        showLoading();
        CompletableFuture<Void> future = new CompletableFuture<>();
        eventsApi.updateEvent(newEvent).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) {
                    Log.d("Calendar","edit event response: success");
                    showToast(getApplicationContext(),"edit event thành công!");
                    CalendarUtils.loadTimetableForUser(getApplicationContext(),getSharedPref(getApplicationContext(), "userId", ""));
                } else {
                    // Xử lý khi response body là false
                    showToast(getApplicationContext(),"Không thể edit event");
                }
                future.complete(null);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public CompletableFuture<Void> deleteEvent(String timetableId, String eventId) {
        showLoading();
        CompletableFuture<Void> future = new CompletableFuture<>();
        eventsApi.deleteEvent(timetableId,eventId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.body() == true) {
                        Log.d("Calendar","delete event response: success");
                        showToast(getApplicationContext(),"Xoá event thành công!");
                        CalendarUtils.loadTimetableForUser(getApplicationContext(),getSharedPref(getApplicationContext(), "userId", ""));
                    } else {
                        // Xử lý khi response body là false
                        showToast(getApplicationContext(),"Không thể xoá event");
                    }
                    future.complete(null);
                } catch (Exception e){
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showToast(getApplicationContext(),"Lỗi server!");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ",t);
                future.completeExceptionally(t);
            }
        });
        return future;
    }
    private void showLoading() {
        binding.progressBar2.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressBar2.setVisibility(View.GONE);
    }
}