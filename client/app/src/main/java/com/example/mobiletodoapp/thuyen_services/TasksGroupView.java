package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mobiletodoapp.R;

import java.util.ArrayList;
import java.util.List;

public class TasksGroupView extends AppCompatActivity {

    RecyclerView rcvTaskList;
    ImageButton btnAddTask;
    ImageView btnBackToPrevious;

    List<Task> tasks = new ArrayList<>();

    private final TaskAdapter taskAdapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {

        }

        @Override
        public void handleCompleteBtn(Task task) {
            if(task.isCompleted()) {
                task.setCompleted(false);
            } else {
                task.setCompleted(true);
            }
            taskAdapter.setData(tasks);
        }

        @Override
        public void handleImportantBtn(Task task) {

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_group_view);

        init();

        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setHasFixedSize(true);
        taskAdapter.setData(tasks);
        rcvTaskList.setAdapter(taskAdapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        rcvTaskList = findViewById(R.id.rcv_task_list);
        btnAddTask = findViewById(R.id.btn_add_task);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);

        tasks = new ArrayList<>();
        tasks.add(new Task("Task1", Task.CHECK, "bla", Task.CHECK));
        tasks.add(new Task("Task2", Task.CHECK, "bla", Task.UNCHECK));
        tasks.add(new Task("Task3", Task.CHECK, "bla", Task.UNCHECK));
    }

}