package com.example.mobiletodoapp.trung_activity;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.saveSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.daysInMonthArray;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.monthYearFromDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.taskDayApi;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.timetableApi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskDayApi;
import com.example.mobiletodoapp.phuc_activity.api.TimetableApi;
import com.example.mobiletodoapp.phuc_activity.dto.TaskDay;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MonthViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView taskDaysRecyclerView;

    public BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout mainCalendarView;

    private ProgressBar progressBar;
    private Button btnAddEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
        initWidgets();

        setMonthView();


        // updateExistingTimetableList(); ko can dung nua
    }


    private void initWidgets()
    {
        progressBar = findViewById(R.id.progressBar);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        taskDaysRecyclerView = findViewById(R.id.taskDaysRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setHalfExpandedRatio(0.6f);
        mainCalendarView = findViewById(R.id.mainCalendarView);
        mainCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progressBar.isAnimating()){

                }else{
                    Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
            showLoading();
            handleTimetableForDate(date);
        }
    }

    private CompletableFuture<Void> handleTimetableForDate(LocalDate date) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String userId = getSharedPref(MonthViewActivity.this,"userId","");
        Timetable timetable = new Timetable(userId,CalendarUtils.monthDayYearDate(date));
        timetableApi.createTimetable(timetable).enqueue(new Callback<Timetable>() {
            @Override
            public void onResponse(Call<Timetable> call, Response<Timetable> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            // Xử lý dữ liệu JSON khi nó không null
//                            saveSharedPref(MonthViewActivity.this, "chosenTimetableId", response.body().getId());
//                            showToast(getApplicationContext(),getSharedPref(getApplicationContext(),"chosenTimetableId",""));
                            loadTaskDaysForDate(response.body().getId());
                        } else {
                            // Xử lý khi response body là null
                            showToast(getApplicationContext(),"Date format bị sai");
                        }
                    } else {
                        showToast(MonthViewActivity.this, "Lỗi server: " + response.code());
                    }
                    hideLoading();
                    future.complete(null);
                } catch (Exception e){
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Timetable> call, Throwable t) {
                hideLoading();
                showToast(getApplicationContext(),"Không tạo được timetable ở hàm check ở BE");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ",t);
                future.completeExceptionally(t);
            }
            });

        return future;
    }

    private CompletableFuture<Void> loadTaskDaysForDate(String timetableId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        showLoading();
        taskDayApi.getListTaskDay(timetableId).enqueue(new Callback<List<TaskDay>>() {
            @Override
            public void onResponse(Call<List<TaskDay>> call, Response<List<TaskDay>> response) {
                try {
                    if (response.body() != null) {
                        TaskDayAdapter adapter = new TaskDayAdapter(response.body());
                        showToast(getApplicationContext(),response.body().toString());
                        Log.d("TaskDay",response.body().get(0).getTitle());
                        taskDaysRecyclerView.setAdapter(adapter);
                        taskDaysRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        // Xử lý khi response body là null
                        showToast(getApplicationContext(),"TaskDay List bị null");
                    }
                    hideLoading();
                    future.complete(null);
                } catch (Exception e){
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<List<TaskDay>> call, Throwable t) {
                hideLoading();
                showToast(getApplicationContext(),"Không lấy đc taskday list");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ",t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }


    private CompletableFuture<Void> updateExistingTimetableList() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String userId = getSharedPref(getApplicationContext(),"userId","");
        timetableApi.getTimetableById(userId).enqueue(new Callback<List<Timetable>>() {
            @Override
            public void onResponse(Call<List<Timetable>> call, Response<List<Timetable>> response) {
                try {
                    CalendarUtils.existingTimetableList = response.body();
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<List<Timetable>> call, Throwable t) {
                hideLoading();
                showToast(getApplicationContext(), "Không thể cập nhật danh sách Timetable");
                Logger.getLogger(MonthViewActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }


    public void weeklyAction(View view)
    {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}


