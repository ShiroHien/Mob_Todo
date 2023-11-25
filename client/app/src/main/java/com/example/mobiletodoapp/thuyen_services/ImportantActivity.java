package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.Task;

import java.util.ArrayList;
import java.util.List;

public class ImportantActivity extends AppCompatActivity {

    List<Task> tasks = new ArrayList<>();
    List<Task> previousTasks = new ArrayList<>();
    RecyclerView rcvTaskList;
    ImageButton btnAddTask;
    ImageView btnBackToPrevious;
    Button btnUndo;

    private final TaskAdapter adapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {
            Toast.makeText(ImportantActivity.this, "click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void handleCompleteBtn(Task task) {
            copyData(previousTasks, tasks);
            if(task.isCompleted() == false) {
                task.setCompleted(true);
            } else {
                task.setCompleted(false);
            }

            tasks.remove(task);
            adapter.setData(tasks);
            btnUndo.setVisibility(View.VISIBLE);
        }

        @Override
        public void handleImportantBtn(Task task) {
            copyData(previousTasks, tasks);
            if(task.isImportant() == false) {
                task.setImportant(true);
            } else {
                task.setImportant(false);
            }
            tasks.remove(task);
            adapter.setData(tasks);
            Toast.makeText(ImportantActivity.this, "Đã xóa nhiệm vụ quan trọng!", Toast.LENGTH_SHORT).show();
            btnUndo.setVisibility(View.VISIBLE);
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important);

        init();

        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setHasFixedSize(true);
        adapter.setData(tasks);
        rcvTaskList.setAdapter(adapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyData(tasks, previousTasks);
                adapter.setData(tasks);
                btnUndo.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        btnAddTask = findViewById(R.id.btn_add_task);
        rcvTaskList = findViewById(R.id.rcv_ipmortant_list);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnUndo = findViewById(R.id.btn_undo);




    }
    public void copyData(List<Task> a, List<Task> b) {
        a.clear();


    }
}