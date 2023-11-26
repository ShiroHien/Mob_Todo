package com.example.mobiletodoapp.thuyen_services;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> mTask;


    private IClickTaskItem iClickTaskItem;

    public interface IClickTaskItem {
        void moveToTaskView(Task task);

        void handleCompleteBtn(Task task);

        void handleImportantBtn(Task task);
    }

    public TaskAdapter(IClickTaskItem iClickTaskItem) {
        this.iClickTaskItem = iClickTaskItem;
    }

    public void setData(List<Task> tasks) {
        this.mTask = tasks;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mTask.get(position);
        if (task == null) {
            return;
        }

        holder.tvTitle.setText(task.getTitle());
        if (task.isCompleted() == true) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btnCheckComplete.setImageResource(R.drawable.radio_button_checked_icon_20);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.btnCheckComplete.setImageResource(R.drawable.radio_button_unchecked_icon_20);
        }
        holder.tvDescription.setText(task.getDescription());
        if(task.isImportant() == true) {
            holder.btnImportant.setImageResource(R.drawable.black_star_important);
        } else {
            holder.btnImportant.setImageResource(R.drawable.white_star_important);
        }

        holder.clTaskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTaskItem.moveToTaskView(task);
            }
        });
        holder.btnCheckComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("adapter", "click check btn");
                iClickTaskItem.handleCompleteBtn(task);
            }
        });

        holder.btnImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTaskItem.handleImportantBtn(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTask != null) {
            return mTask.size();
        }
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView btnCheckComplete;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView btnImportant;
        private ConstraintLayout clTaskItem;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            btnCheckComplete = itemView.findViewById(R.id.btn_check_completed);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDescription = itemView.findViewById(R.id.tv_task_description);
            btnImportant = itemView.findViewById(R.id.btn_important);
            clTaskItem = itemView.findViewById(R.id.cl_task_item);
        }
    }

}
