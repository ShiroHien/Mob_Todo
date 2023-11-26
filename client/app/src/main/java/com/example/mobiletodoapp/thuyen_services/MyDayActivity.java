package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Task;

import java.util.ArrayList;
import java.util.List;

public class MyDayActivity extends AppCompatActivity {
    ImageView btnBackToPrevious;
    RecyclerView rcvUncompletedList;
    RecyclerView rcvCompletedList;
    ConstraintLayout line;
    ImageButton btnAddTask;

    List<Task> tasks;
    List<Task> completedTasks = new ArrayList<>();
    List<Task> uncompletedTasks = new ArrayList<>();

    private final TaskAdapter completedAdapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {

        }

        @Override
        public void handleCompleteBtn(Task task) {
            handleCompleteButton(task);
        }

        @Override
        public void handleImportantBtn(Task task) {
            handleImportantButton(task);
        }
    });

    private final TaskAdapter uncompletedAdapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {

        }

        @Override
        public void handleCompleteBtn(Task task) {
            handleCompleteButton(task);
        }

        @Override
        public void handleImportantBtn(Task task) {
            handleImportantButton(task);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);
        init();

        rcvCompletedList.setLayoutManager(new LinearLayoutManager(this));
        rcvCompletedList.setHasFixedSize(true);
        completedAdapter.setData(completedTasks);
        rcvCompletedList.setAdapter(completedAdapter);

        rcvUncompletedList.setLayoutManager(new LinearLayoutManager(this));
        rcvUncompletedList.setHasFixedSize(true);
        uncompletedAdapter.setData(uncompletedTasks);
        rcvUncompletedList.setAdapter(uncompletedAdapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        rcvUncompletedList = findViewById(R.id.rcv_uncompleted_list);
        rcvCompletedList = findViewById(R.id.rcv_completed_list);
        btnAddTask = findViewById(R.id.btn_add_task);
        line = findViewById(R.id.line);

        tasks = new ArrayList<>();


        for(Task t : tasks) {
            if(t.isCompleted() == true) {
                completedTasks.add(t);
            } else {
                uncompletedTasks.add(t);
            }
        }
    }

    private void handleCompleteButton(Task task) {
        Toast.makeText(this, "click check byn", Toast.LENGTH_SHORT).show();
        if(task.isCompleted() == true) {
            uncompletedTasks.add(task);
            completedTasks.remove(task);
            task.setCompleted(false);
            showLine();

            uncompletedAdapter.setData(uncompletedTasks);
            completedAdapter.setData(completedTasks);
        } else {
            completedTasks.add(task);
            uncompletedTasks.remove(task);
            task.setCompleted(true);
            showLine();

            uncompletedAdapter.setData(uncompletedTasks);
            completedAdapter.setData(completedTasks);
        }
    }

    private void showLine() {
        if(completedTasks.isEmpty()) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }
    }

    private void handleImportantButton(Task task) {
        task.setImportant(!task.isImportant());
        uncompletedAdapter.setData(uncompletedTasks);
        completedAdapter.setData(completedTasks);
    }

}