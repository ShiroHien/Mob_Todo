package com.example.mobiletodoapp.thuyen_services;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;

import java.util.List;

public class TasksGroupAdapter extends RecyclerView.Adapter<TasksGroupAdapter.TasksGroupViewHolder> {

    private List<TasksGroup> mTasksGroup;

    private IClickTasksGroupItem iClickTasksGroupItem;
    public interface IClickTasksGroupItem {
        void moveToTaskGroupView(TasksGroup tasksGroup);
    }

    public TasksGroupAdapter(IClickTasksGroupItem iClickTasksGroupItem) {
        this.iClickTasksGroupItem = iClickTasksGroupItem;
    }

    public void setData(List<TasksGroup> tasksGroups) {
        Log.d("adapter", Integer.toString(tasksGroups.size()));
        this.mTasksGroup = tasksGroups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TasksGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasksgroup_item, parent, false);
        return new TasksGroupViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TasksGroupViewHolder holder, int position) {
        TasksGroup tasksGroup = mTasksGroup.get(position);
        if(tasksGroup == null) {
            return;
        }
        holder.tvTitle.setText(tasksGroup.getTitle());
        holder.llTasksGroupsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTasksGroupItem.moveToTaskGroupView(tasksGroup);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mTasksGroup != null) {
            return mTasksGroup.size();
        }
        return 0;
    }

    public class TasksGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private LinearLayout llTasksGroupsItem;
        public TasksGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            llTasksGroupsItem = itemView.findViewById(R.id.ll_tasksgroup_item);
        }
    }
}