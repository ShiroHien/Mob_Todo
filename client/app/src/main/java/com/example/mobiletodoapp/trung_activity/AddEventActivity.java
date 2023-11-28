package com.example.mobiletodoapp.trung_activity;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.eventsApi;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.scaleAnimation;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedTimetableId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Events;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    ProgressBar progressBar;
    private Switch switchFullDay;

    private TextView startTime, endTime;
    private LinearLayout startTimeContainer, endTimeContainer;
    private Button btnAdd;
    int startHour,startMin,endHour,endMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initWidgets();
    }


    private void initWidgets() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        switchFullDay = findViewById(R.id.switchFullDay);
        startTime = findViewById(R.id.etStartTime);
        endTime = findViewById(R.id.etEndTime);
        startTimeContainer = findViewById(R.id.startTimeContainer);
        endTimeContainer = findViewById(R.id.endTimeContainer);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar2);
        hideLoading();


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAnimation(view);
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        startHour = h;
                        startMin = m;
                        startTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,startHour,startMin,true);
                timePickerDialog.setTitle("Chọn giở bắt đầu");
                timePickerDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAnimation(view);
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        endHour = h;
                        endMin = m;
                        endTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,endHour,endMin,true);
                timePickerDialog.setTitle("Chọn giở kết thúc");
                timePickerDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progressBar.isAnimating() || CalendarUtils.creatingTimetable){
                    Toast.makeText(getApplicationContext(),"Vui lòng thao tác chậm lại!",Toast.LENGTH_SHORT).show();
                }
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                Events newEvent = new Events(selectedTimetableId,title,description,startTime.getText().toString(),endTime.getText().toString());

                showLoading();
                addNewEvent(newEvent).whenComplete((result,throwable) -> {
                    hideLoading();
                    onBackPressed();
                });
            }
        });

        switchFullDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchFullDay.isChecked()){
                    startTime.setText("00:00");
                    endTime.setText("23:59");
                    startTime.setVisibility(View.GONE);
                    startTime.setTextColor(Color.DKGRAY);
                    endTime.setTextColor(Color.DKGRAY);
                    endTime.setVisibility(View.GONE);
                }else{
                    startTime.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                    startTime.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.main_color));
                    endTime.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.main_color));
                }
            }
        });
    }

    public CompletableFuture<Void> addNewEvent(Events newEvent) {
        showLoading();
        CompletableFuture<Void> future = new CompletableFuture<>();
        eventsApi.createEvent(newEvent).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.body() == true) {
                        Log.d("Calendar","add event response: success");
                        showToast(getApplicationContext(),"Thêm event thành công!");
                        CalendarUtils.loadTimetableForUser(getApplicationContext(),getSharedPref(getApplicationContext(), "userId", ""));
                    } else {
                        // Xử lý khi response body là false
                        showToast(getApplicationContext(),"Không thể thêm event mới");
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
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}