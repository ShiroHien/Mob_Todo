package com.mob.todoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mob.todoapp.model.TasksGroup;
import com.mob.todoapp.ui.AddTasksGroupActivity;
import com.mob.todoapp.ui.TasksGroupAdapter;
import com.mob.todoapp.ui.TasksGroupView;
import com.mob.todoapp.viewmodel.TasksGroupViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TASKSGROUP_TITLE = "com.mob.todoapp.ui.tasksgroup_title";
    public static final String TASKSGROUP_ID = "com.mob.todoapp.ui.tasksgroup_id";
    public static final String TASKSGROUP = "com.mob.todoapp.ui.tasksgroup";
    private TasksGroupViewModel tasksGroupViewModel;

    FloatingActionButton btnAddTasksgroup;

    private final TasksGroupAdapter adapter = new TasksGroupAdapter(new TasksGroupAdapter.IClickTasksGroupItem() {
        @Override
        public void moveToTaskGroupView(TasksGroup tasksGroup) {
            movToTaskGroupView(tasksGroup);
        }
    });
    ActivityResultLauncher<Intent> addTasksGroupLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String tasksGroupTitle = data.getStringExtra(TASKSGROUP_TITLE);
                    tasksGroupViewModel.insert(new TasksGroup(tasksGroupTitle));
                } else {
                    // Xử lý khi hoạt động con bị hủy hoặc gặp lỗi
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rcv_tasks_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(adapter);

        tasksGroupViewModel = new ViewModelProvider(this).get(TasksGroupViewModel.class);
        tasksGroupViewModel.getAllTasksGroup().observe(MainActivity.this, new Observer<List<TasksGroup>>() {
            @Override
            public void onChanged(List<TasksGroup> tasksGroups) {
//                Toast.makeText(MainActivity.this, Integer.toString(tasksGroups.size()), Toast.LENGTH_SHORT).show();
                adapter.setData(tasksGroups);
            }
        });

        btnAddTasksgroup = findViewById(R.id.btn_add_tasksgroup);
        btnAddTasksgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTasksGroupActivity.class);
                addTasksGroupLauncher.launch(intent);
            }
        });
    }



    private void movToTaskGroupView(TasksGroup tasksGroup) {
        Intent intent = new Intent(MainActivity.this, TasksGroupView.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASKSGROUP, tasksGroup);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public TasksGroupViewModel getTasksGroupViewModel() {
        return tasksGroupViewModel;
    }

}