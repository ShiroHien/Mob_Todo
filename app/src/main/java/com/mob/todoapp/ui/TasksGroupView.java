package com.mob.todoapp.ui;

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
        String title = intent.getStringExtra(MainActivity.TASKSGROUP_NAME).toString();
        toolbarGroupView.setTitle(title);


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

        } else if(id == R.id.delete_group) {
            TasksGroup tasksGroup = null;
            List<TasksGroup> tasksGroups = tasksGroupViewModel.getAllTasksGroup().getValue();
            for(int i = 0; i < tasksGroups.size(); i++) {
                if (tasksGroups.get(i).getId() == Integer.parseInt(intent.getStringExtra(MainActivity.TASKSGROUP_ID))) {
                    tasksGroup = tasksGroups.get(i);
                    break;
                }
            }
            tasksGroupViewModel.delete(tasksGroup);

//            Intent intent = new Intent();
//            setResult(Activity.RESULT_OK, intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}