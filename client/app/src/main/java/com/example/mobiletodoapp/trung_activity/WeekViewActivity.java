package com.example.mobiletodoapp.trung_activity;


import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.daysInWeekArray;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.monthYearFromDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedDate;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedTimetableId;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView eventsRecyclerView;

    private EventsAdapter eventsAdapter;
    private FloatingActionButton btnAddEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventsRecyclerView = findViewById(R.id.rvEvents);
        eventsAdapter = new EventsAdapter(new ArrayList<>());
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null){
            CalendarUtils.selectedDate = date;
            setWeekView();
            handleTimetableForDate(date);
        }

    }

    private void handleTimetableForDate(LocalDate selectedDate) {
        for(int i = 0; i< CalendarUtils.existingTimetableList.size(); i++){
            Timetable timetableItem = CalendarUtils.existingTimetableList.get(i);

            if(timetableItem.getDayTime().equals(CalendarUtils.monthDayYearDate(selectedDate))){
                selectedTimetableId = timetableItem.getId();
                Toast.makeText(this,selectedTimetableId,Toast.LENGTH_SHORT).show();
                eventsAdapter.updateEventsList(timetableItem.getEvents());
                return;
            }
        }
        Toast.makeText(this,"Không có sự kiện nào trong ngày này",Toast.LENGTH_SHORT).show();
        selectedTimetableId = null;
        eventsAdapter.updateEventsList(new ArrayList<>());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


}