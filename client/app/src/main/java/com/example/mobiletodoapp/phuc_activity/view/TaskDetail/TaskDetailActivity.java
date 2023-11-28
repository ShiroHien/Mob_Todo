package com.example.mobiletodoapp.phuc_activity.view.TaskDetail;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.hideLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends AppCompatActivity {
    ImageView btnBackToPrevious;
    TextView taskgroupName, startTime, endTime;
    EditText task_title, description;
    private RetrofitService retrofitService;
    private TaskApi taskApi;
    private ProgressDialog progressDialog;
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
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);
        getDetail(taskApi, intent.getStringExtra("taskId"));
    }
    private CompletableFuture<Void> getDetail(TaskApi taskApi, String taskId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        taskApi.getDetailTask(taskId).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                try {
                    if (response.isSuccessful()) {
                        Task result = response.body();
                        if(result != null) {
                            description.setText(result.getDescription());
                            startTime.setText(result.getStartTime());
                            endTime.setText(result.getEndTime());
                        } else {
                            showToast(TaskDetailActivity.this, "Lấy dữ liệu không thành công");
                        }
                    } else {
                        showToast(TaskDetailActivity.this, "Lấy dữ liệu không thành công");
                    }
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.d("error", "loi gi do", t);
                showToast(TaskDetailActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}