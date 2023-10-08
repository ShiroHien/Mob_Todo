package com.mob.todoapp.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mob.todoapp.MainActivity;
import com.mob.todoapp.R;
import com.mob.todoapp.model.TasksGroup;
import com.mob.todoapp.viewmodel.TasksGroupViewModel;

import java.util.List;

public class TasksGroupView extends AppCompatActivity {

    Toolbar toolbarGroupView;
    Intent intent;

    TasksGroupViewModel tasksGroupViewModel;
    TasksGroup tasksGroup;

    ActivityResultLauncher<Intent> updateTasksGroupLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String newTitle = data.getStringExtra(MainActivity.TASKSGROUP_TITLE);
                    tasksGroup.setTitle(newTitle);
                    tasksGroupViewModel.update(tasksGroup);
                    toolbarGroupView.setTitle(newTitle);
                } else {
                    // Xử lý khi hoạt động con bị hủy hoặc gặp lỗi
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_group_view);

        tasksGroupViewModel = new ViewModelProvider(this).get(TasksGroupViewModel.class);
        tasksGroupViewModel.getAllTasksGroup().observe(TasksGroupView.this, new Observer<List<TasksGroup>>() {
            @Override
            public void onChanged(List<TasksGroup> tasksGroups) {
                Log.d("on changed", Integer.toString(tasksGroups.size()));
            }
        });

        toolbarGroupView = findViewById(R.id.tb_group_view);
        setSupportActionBar(toolbarGroupView);

        intent = this.getIntent();
        tasksGroup = (TasksGroup) intent.getExtras().get(MainActivity.TASKSGROUP);
        toolbarGroupView.setTitle(tasksGroup.getTitle());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.rename_group) {
            Intent intent = new Intent(TasksGroupView.this, UpdateTasksGroupAcitivity.class);
            intent.putExtra(MainActivity.TASKSGROUP_TITLE, tasksGroup.getTitle().toString());
            updateTasksGroupLauncher.launch(intent);

        } else if(id == R.id.delete_group) {
            tasksGroupViewModel.delete(tasksGroup);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}