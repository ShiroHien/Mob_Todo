package com.example.mobiletodoapp.trung_activity;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.databinding.EventItemBinding;
import com.example.mobiletodoapp.phuc_activity.dto.Events;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.TaskDayViewHolder>{
    private List<Events> eventsList;
    private OnItemClickListener onItemClickListener;

    public EventsAdapter(List<Events> eventsList) {
        this.eventsList = eventsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public TaskDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventItemBinding binding = EventItemBinding.inflate(inflater,parent,false);
        return new TaskDayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDayViewHolder holder, int position) {
        Events item = eventsList.get(position);
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvStartTime.setText(item.getStartTime());
        holder.binding.tvEndTime.setText(item.getEndTime());
        holder.binding.tvDescription.setText(item.getDescription());

        // Set click listener
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEventItemClick(item);
            }
        });
        CalendarUtils.fadeInAnimation(holder.itemView,500);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


    public class TaskDayViewHolder extends RecyclerView.ViewHolder {
        private final EventItemBinding binding;


        public TaskDayViewHolder(EventItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateEventsList(List<Events> newList){
        this.eventsList = newList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEventItemClick(Events event);
    }
}
