package com.example.mobiletodoapp.trung_activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.databinding.TaskDayItemBinding;
import com.example.mobiletodoapp.phuc_activity.dto.TaskDay;

import java.util.List;

public class TaskDayAdapter extends RecyclerView.Adapter<TaskDayAdapter.TaskDayViewHolder>{
    private final List<TaskDay> taskDays;

    public TaskDayAdapter(List<TaskDay> taskDays) {
        this.taskDays = taskDays;
    }

    @NonNull
    @Override
    public TaskDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TaskDayItemBinding binding = TaskDayItemBinding.inflate(inflater,parent,false);
        return new TaskDayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDayViewHolder holder, int position) {
        TaskDay item = taskDays.get(position);
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvStartTime.setText(item.getStartTime());
        holder.binding.tvEndTime.setText(item.getEndTime());
    }

    @Override
    public int getItemCount() {
        return taskDays.size();
    }

    public class TaskDayViewHolder extends RecyclerView.ViewHolder {
        private final TaskDayItemBinding binding;


        public TaskDayViewHolder(TaskDayItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}
