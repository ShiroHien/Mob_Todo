package com.example.mobiletodoapp.phuc_activity.view.TaskDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;

public class TaskDetailActivity extends AppCompatActivity {
    ImageView btnBackToPrevious;
    TextView taskgroupName;
    EditText task_title, description;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = this.getIntent();
        Toast.makeText(this, intent.getStringExtra("taskId") + '\n' +
                intent.getStringExtra("taskTitle") + '\n' +
                intent.getStringExtra("taskgroupTitle"), Toast.LENGTH_SHORT).show();
        init();
        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init() {
        Intent intent = this.getIntent();
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        taskgroupName = findViewById(R.id.taskgroupName);
        taskgroupName.setText(intent.getStringExtra("taskgroupTitle"));
        task_title = findViewById(R.id.tv_task_title);
        task_title.setText(intent.getStringExtra("taskTitle"));
        description = findViewById(R.id.description);

    }
}