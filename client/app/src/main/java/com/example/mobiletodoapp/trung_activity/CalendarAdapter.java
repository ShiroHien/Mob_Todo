package com.example.mobiletodoapp.trung_activity;


import static com.example.mobiletodoapp.trung_activity.CalendarUtils.fadeInAnimation;
import static com.example.mobiletodoapp.trung_activity.CalendarUtils.selectedTimetableId;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);
        if(date == null)
            holder.dayOfMonth.setText("");
        else
        {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(CalendarUtils.selectedDate)){
                holder.parentView.setBackgroundColor(Color.LTGRAY);
                holder.dayOfMonth.setTextColor(Color.WHITE);
            }

            for(int i = 0; i< CalendarUtils.existingTimetableList.size(); i++){
                Timetable timetableItem = CalendarUtils.existingTimetableList.get(i);

                if(timetableItem.getDayTime().equals(CalendarUtils.monthDayYearDate(date))){
                    if(!timetableItem.getEvents().isEmpty()){
                        holder.iwHasEvent.setVisibility(View.VISIBLE);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}