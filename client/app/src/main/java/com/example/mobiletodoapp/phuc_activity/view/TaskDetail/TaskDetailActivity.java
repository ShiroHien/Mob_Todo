package com.example.mobiletodoapp.phuc_activity.view.TaskDetail;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.hideLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showLoading;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends AppCompatActivity {
    ImageView btnBackToPrevious, btn_pick_time_start, btn_pick_time_end, btnDelete, btnComplete;
    TextView taskgroupName, startTime, endTime, text_myday, text_important, text_save;
    EditText task_title, description;
    private RetrofitService retrofitService;
    private TaskApi taskApi;
    private Boolean isMyday = false, isImportant = false, isCompleted = false;
    private String taskGroupId = "";
    LinearLayout add_myday, add_important;
    Boolean isShowedDialogFragment = false, isShowTextSaved = false;
    ConstraintLayout layoutDeleteConfirm;
    Button delete, cancel, btnSave;

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
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isShowTextSaved == false) {
                    isShowTextSaved = true;
                    text_save.setVisibility(View.VISIBLE);
                }
                callUpdateTaskApi(taskId, taskGroupId);
            }
        };
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
        btn_pick_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startTime);
            }
        });
        btn_pick_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endTime);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    isShowedDialogFragment = true;
                    layoutDeleteConfirm.setVisibility(View.VISIBLE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDeleteConfirm.setVisibility(View.GONE);
                isShowedDialogFragment = false;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskApi, taskId);
            }
        });

        task_title.addTextChangedListener(textWatcher);
        description.addTextChangedListener(textWatcher);
        startTime.addTextChangedListener(textWatcher);
        endTime.addTextChangedListener(textWatcher);
        CompletableFuture<Void> getDetailFuture = getDetail(taskApi, taskId);
        getDetailFuture.thenRun(() -> {
            getDetailFuture.thenRun(() -> {
                callUpdateTaskApi(taskId, taskGroupId);
            });
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
        btn_pick_time_start = findViewById(R.id.btn_pick_start_time);
        btn_pick_time_end = findViewById(R.id.btn_pick_end_time);
        btnDelete = findViewById(R.id.btnDelete);
        layoutDeleteConfirm = findViewById(R.id.layout_delete_confirm);
        delete = findViewById(R.id.delete);
        cancel = findViewById(R.id.cancel);
        text_save = findViewById(R.id.text_save);
        btnComplete = findViewById(R.id.btn_check_completed);

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
                            taskGroupId = result.getTaskGroupId();
                            if (result.isMyDay()) {
                                isMyday = true;
                                text_myday.setTextColor(Color.parseColor("#3700b3"));
                            }
                            if (result.isImportant()) {
                                isImportant = true;
                                text_important.setTextColor(Color.parseColor("#3700b3"));
                            }
                            if (result.isCompleted()) {
                                isCompleted = true;
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
                                showToast(TaskDetailActivity.this, "Thêm vào quan trọng thành công");
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
                        showToast(TaskDetailActivity.this, "Xử lý không thành công(response successful)");
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

    private void showDatePickerDialog(TextView dateTime) {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String stringDate = (month + 1) + "/" + dayOfMonth + "/" + year;

                        showTimePickerDialog(dateTime, stringDate);
                    }
                },
                year, month, day
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TextView dateTime, String stringDate) {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Xử lý khi người dùng chọn thời gian
                        String selectedTime = hourOfDay + ":" + minute + ":00";

                        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date;
                        try {
                            date = inputFormat.parse(selectedTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }

                        // Format the Date object to the desired output format
                        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = outputFormat.format(date);

                        dateTime.setText(stringDate + " " + formattedTime);
                    }
                },
                hour, minute, true // true để sử dụng 24-giờ, false để sử dụng AM/PM
        );

        // Hiển thị TimePickerDialog
        timePickerDialog.show();
    }

    private CompletableFuture<Void> deleteTask(TaskApi taskApi, String taskId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        taskApi.deleteTask(taskId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.body()) {
                        showToast(TaskDetailActivity.this, "Xoá thành công");
                        finish();
                    } else {
                        showToast(TaskDetailActivity.this, "Xoá thất bại");
                    }
                    future.complete(null);
                } catch (Exception e) {
                    showToast(TaskDetailActivity.this, "Lỗi xảy ra");
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showToast(TaskDetailActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    private CompletableFuture<Void> updateTask(TaskApi taskApi, String taskId, String taskGroupId, String title, String description, String start_Time, String end_Time, Boolean completed, Boolean myDay, Boolean important) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Task data = new Task(taskId, taskGroupId, title, description, start_Time, end_Time, completed, myDay, important);
        taskApi.updateTask(data).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result == true) {
                            if (isShowTextSaved == true) {
                                isShowTextSaved = false;
                                text_save.setVisibility(View.GONE);
                            }
                        } else {

                        }
                    } else {
                        showToast(TaskDetailActivity.this, "Cập nhập không thành công");
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

    private void callUpdateTaskApi(String taskId, String taskGroupId) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> completableFuture = new CompletableFuture<>();

                Future<?> future = executorService.submit(() -> {
                    try {
                        updateTask(taskApi, taskId, taskGroupId, task_title.getText().toString(), description.getText().toString(), startTime.getText().toString(), endTime.getText().toString(), isCompleted, isMyday, isImportant);
                        completableFuture.complete(null);
                    } catch (Exception e) {
                        completableFuture.completeExceptionally(e);
                    }
                });

                try {
                    future.get(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    showToast(TaskDetailActivity.this, "Timeout khi gọi API");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

        executorService.shutdown();
//            }
//        } catch (Exception e) {
//            showToast(TaskDetailActivity.this, "Có lỗi xảy ra");
//        }
    }

}