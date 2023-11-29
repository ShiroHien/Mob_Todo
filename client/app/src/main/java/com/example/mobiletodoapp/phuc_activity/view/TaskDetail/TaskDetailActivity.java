package com.example.mobiletodoapp.phuc_activity.view.TaskDetail;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.hideLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    TextView taskgroupName, startTime, endTime, text_myday, text_important;
    EditText task_title, description;
    private RetrofitService retrofitService;
    private TaskApi taskApi;
    private Boolean isMyday = false, isImportant = false;
    LinearLayout add_myday, add_important;
    Boolean isShowedDialogFragment = false;
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
        init();
        String taskId = intent.getStringExtra("taskId");
        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_myday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyDay(taskApi, taskId);
            }
        });
        add_important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImportant(taskApi, taskId);
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
        add_important = findViewById(R.id.add_important);
        add_myday = findViewById(R.id.add_myday);
        text_myday = findViewById(R.id.text_myday);
        text_important = findViewById(R.id.text_important);

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
                        if (result != null) {
                            description.setText(result.getDescription());
                            startTime.setText(result.getStartTime());
                            endTime.setText(result.getEndTime());
                            if (result.isMyDay()) {
                                isMyday = true;
                                text_myday.setTextColor(Color.parseColor("#3700b3"));
                            }
                            if (result.isImportant()) {
                                isMyday = true;
                                text_important.setTextColor(Color.parseColor("#3700b3"));
                            }
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

    private CompletableFuture<Void> setImportant(TaskApi taskApi, String taskId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        taskApi.setImportant(taskId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        System.out.println(result);
                        if (result == true) {
                            if (isImportant) {
                                text_important.setTextColor(Color.parseColor("#000000"));
                                showToast(TaskDetailActivity.this, "Bỏ thành công");
                            } else {
                                text_important.setTextColor(Color.parseColor("#3700b3"));
                                showToast(TaskDetailActivity.this, "Thêm vào ngày của tôi thành công");
                            }
                            isImportant = !isImportant;
                        } else {
                            showToast(TaskDetailActivity.this, "Xử lý không thành công");
                        }
                    } else {
                        showToast(TaskDetailActivity.this, "Thêm vào quan trọng không thành công");
                    }
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("error", "loi gi do", t);
                showToast(TaskDetailActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    private CompletableFuture<Void> setMyDay(TaskApi taskApi, String taskId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        taskApi.setMyDay(taskId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result == true) {
                            if (isMyday) {
                                text_myday.setTextColor(Color.parseColor("#000000"));
                                showToast(TaskDetailActivity.this, "Bỏ thành công");
                            } else {
                                text_myday.setTextColor(Color.parseColor("#3700b3"));
                                showToast(TaskDetailActivity.this, "Thêm vào ngày của tôi thành công");
                            }
                            isMyday = !isMyday;
                        } else {
                            showToast(TaskDetailActivity.this, "Xử lý không thành công");
                        }
                    } else {
                        showToast(TaskDetailActivity.this, "Xử lý không thành công");
                    }
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("error", "loi gi do", t);
                showToast(TaskDetailActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}