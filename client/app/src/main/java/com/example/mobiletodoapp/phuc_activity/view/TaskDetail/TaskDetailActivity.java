package com.example.mobiletodoapp.phuc_activity.view.TaskDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mobiletodoapp.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = this.getIntent();
        Toast.makeText(this, intent.getStringExtra("taskId") + '\n' +
                intent.getStringExtra("taskTitle") + '\n' +
                intent.getStringExtra("taskgroupTitle"), Toast.LENGTH_SHORT).show();
    }
}