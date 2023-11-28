package com.example.mobiletodoapp.trung_activity;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.daysInMonthArray;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.handleTimetableForDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.monthYearFromDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedTimetableId;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Events;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, EventsAdapter.OnItemClickListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;

    public BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout mainCalendarView;

    private ProgressBar progressBar;
    private Button btnAddEvent;
    ImageButton btnNextMonth,btnPreviousMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
        initWidgets();

        setMonthView();

    }


    private void initWidgets()
    {
        progressBar = findViewById(R.id.progressBar);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPreviousMonth = findViewById(R.id.btnPreviousMonth);
        hideLoading();
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        // ban đầu chưa chọn ngày nên chưa có event nào, nên adapter arraylist trống
        eventsAdapter = new EventsAdapter(new ArrayList<>());
        eventsAdapter.setOnItemClickListener(this);
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                if(selectedTimetableId == null){
                    CalendarUtils.creatingTimetable = true;
                    String userId = getSharedPref(getApplicationContext(), "userId", "");
                    Timetable newTimetable = new Timetable(userId,CalendarUtils.monthDayYearDate(selectedDate),new ArrayList<>());
                    CalendarUtils.createTimetableForDate(getApplicationContext(),newTimetable).whenComplete((result,throwable)->{
                        CalendarUtils.creatingTimetable = false;
                    });
                }
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            selectedDate = date;
            setMonthView();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            handleTimetableForDate(date,eventsAdapter);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onEventItemClick(Events event) {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("title",event.getTitle());
        intent.putExtra("description",event.getDescription());
        intent.putExtra("timetableId",event.getTimetableId());
        intent.putExtra("id",event.getId());
        intent.putExtra("startTime",event.getStartTime());
        intent.putExtra("endTime",event.getEndTime());
        startActivity(intent);
    }

}


