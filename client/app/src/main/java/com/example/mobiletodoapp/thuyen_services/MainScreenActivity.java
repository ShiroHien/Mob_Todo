package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    LinearLayout myDay;
    LinearLayout important;
    LinearLayout calendar;
    LinearLayout pomodoro;
    ImageButton btnAddTasksgroup;

    ConstraintLayout layoutAddTasksgroup;

    EditText edtGroupTitle;
    TextView tvCancelAddGroup;
    TextView tvAddGroup;

    RecyclerView recyclerView;



    List<TasksGroup> tasksGroups = new ArrayList<>();

    private final TasksGroupAdapter adapter = new TasksGroupAdapter(new TasksGroupAdapter.IClickTasksGroupItem() {
        @Override
        public void moveToTaskGroupView(TasksGroup tasksGroup) {
            Intent intent = new Intent(MainScreenActivity.this, TasksGroupView.class);
            startActivity(intent);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        init();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter.setData(tasksGroups);
        recyclerView.setAdapter(adapter);

        btnAddTasksgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutAddTasksgroup.setVisibility(View.VISIBLE);
            }
        });
        tvCancelAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGroupTitle.setText("");
                layoutAddTasksgroup.setVisibility(View.GONE);
            }
        });
        tvAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTasksGroup(edtGroupTitle.getText().toString().trim());
            }
        });

        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, ImportantActivity.class);
                startActivity(intent);
            }
        });

        myDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, MyDayActivity.class);
                startActivity(intent);
            }
        });

        pomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, PomodoroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        myDay = findViewById(R.id.my_day);
        important = findViewById(R.id.important);
        calendar = findViewById(R.id.calendar);
        pomodoro = findViewById(R.id.pomodoro);
        btnAddTasksgroup = findViewById(R.id.btn_add_tasksgroup);
        recyclerView = findViewById(R.id.rcv_tasksgroup);

        layoutAddTasksgroup = findViewById(R.id.layout_add_tasksgroup);
        edtGroupTitle = findViewById(R.id.edt_group_title);
        tvCancelAddGroup = findViewById(R.id.tv_cancel_add_tasksgroup);
        tvAddGroup = findViewById(R.id.tv_add_tasksgroup);



        TasksGroup t1 = new TasksGroup("nhom 1");
        TasksGroup t2 = new TasksGroup("nhom 2");
        TasksGroup t3 = new TasksGroup("nhom 3");
        tasksGroups.add(t1);
        tasksGroups.add(t2);
        tasksGroups.add(t3);
    }

    private void addTasksGroup(String title) {
        if(title == null || title.isEmpty()) {
            Toast.makeText(MainScreenActivity.this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            tasksGroups.add(new TasksGroup(title));
            adapter.setData(tasksGroups);
            edtGroupTitle.setText("");
            layoutAddTasksgroup.setVisibility(View.GONE);
        }
    }
}