package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import org.checkerframework.checker.units.qual.C;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TasksGroupView extends AppCompatActivity {

    RecyclerView rcvTaskList;
    ImageButton btnShowAddTaskLayout;
    ImageView btnBackToPrevious;

    TextView tvHeaderTitlte;

    List<Task> tasks = new ArrayList<>();

    Intent intent;

    TaskApi taskApi;

    ProgressDialog progressDialog;
    ConstraintLayout clAddTask;

    EditText edtTaskTitle;
    Spinner spTasksGroupList;

    TextView tvStartTime;
    ImageView btnPickStartTime;
    TextView tvEndTime;
    ImageView btnPickEndTime;
    EditText edtDescription;

    TextView btnCancelAddTask;
    TextView btnAddTask;

    String tasksGroupIdSelected;


    private final TaskAdapter taskAdapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {

        }

        @Override
        public void handleCompleteBtn(Task task) {
            if (task.isCompleted()) {
                task.setCompleted(false);
            } else {
                task.setCompleted(true);
            }
            taskAdapter.setData(tasks);
        }

        @Override
        public void handleImportantBtn(Task task) {

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_group_view);

        init();
        taskApi = new RetrofitService().getRetrofit().create(TaskApi.class);
        showLoading();
        getTaskListFromServer(taskApi);

        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setHasFixedSize(true);
        taskAdapter.setData(tasks);
        rcvTaskList.setAdapter(taskAdapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnShowAddTaskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleShowAddTaskLayout();
            }
        });

        btnPickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvStartTime);
            }
        });

        btnPickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvEndTime);
            }
        });

        btnCancelAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTaskTitle.setText("");
                clAddTask.setVisibility(View.GONE);
                edtDescription.setText("");
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickedBtnAddTask();
            }
        });


    }

    private void init() {
        rcvTaskList = findViewById(R.id.rcv_task_list);
        btnShowAddTaskLayout = findViewById(R.id.btn_show_add_task_layout);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        tvHeaderTitlte = findViewById(R.id.tv_header_title);
        clAddTask = findViewById(R.id.inc_add_task_layout);

        edtTaskTitle = findViewById(R.id.edt_task_title);
        spTasksGroupList = findViewById(R.id.sp_tasksgroup_list);
        tvStartTime = findViewById(R.id.tv_start_time);
        btnPickStartTime = findViewById(R.id.btn_pick_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        btnPickEndTime = findViewById(R.id.btn_pick_end_time);
        edtDescription = findViewById(R.id.edt_description);
        btnCancelAddTask = findViewById(R.id.btn_cancel_add_task);
        btnAddTask = findViewById(R.id.btn_add_task);


        intent = this.getIntent();

        tvHeaderTitlte.setText(intent.getStringExtra("tasksgroupTitle"));

    }

    private void handleClickedBtnAddTask() {
        String title = edtTaskTitle.getText().toString().trim();
        String des = edtDescription.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        if (title == null || title.isEmpty()) {
            Toast.makeText(TasksGroupView.this, "Tên nhiệm vụ không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            Task task = new Task(tasksGroupIdSelected, title, des, startTime, endTime);
            createTask(taskApi, task);
            clAddTask.setVisibility(View.GONE);
        }
    }

    private CompletableFuture<Void> createTask(TaskApi taskApi, Task task) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Log.d("create task", tasksGroupIdSelected + '\n' +
                task.getTitle() + '\n' +
                task.getDescription() + '\n' +
                task.getStartTime() + '\n' +
                task.getEndTime());

        taskApi.createTask(task).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.body()) {
                        tasks.add(task);
                        taskAdapter.setData(tasks);
                        hideLoading();
                        Log.d("create task", "them nv thanh cong");
                    } else {
                        Log.d("create task", "them nv that bai");
                    }
                } catch (Exception e) {

                    future.completeExceptionally(e);
                    Log.d("create task", "loi ket noi");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    private CompletableFuture<Void> getTaskListFromServer(TaskApi taskApi) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        taskApi.getTasks(intent.getStringExtra("tasksgroupId")).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                try {
                    tasks = response.body();
                    taskAdapter.setData(tasks);
                    hideLoading();

                    future.complete(null);
                } catch (Exception e) {

                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
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

    private void handleShowAddTaskLayout() {
        clAddTask.setVisibility(View.VISIBLE);


        List<TaskGroup> taskGroups = new ArrayList<>();
        Intent intent1 = this.getIntent();
        taskGroups.add(new TaskGroup(intent1.getStringExtra("tasksgroupTitle"), "1"));


        ArrayAdapter<TaskGroup> spinnerAdapter = new ArrayAdapter<TaskGroup>(TasksGroupView.this,
                android.R.layout.simple_spinner_item, taskGroups);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTasksGroupList.setAdapter(spinnerAdapter);

        spTasksGroupList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskGroup taskGroup = (TaskGroup) parent.getSelectedItem();
                tasksGroupIdSelected = intent1.getStringExtra("tasksgroupId");
                Log.d("create task", "select id: " + intent1.getStringExtra("tasksgroupId"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvStartTime.setText((month + 1) + "/" + day + "/" + year + " 00:00:00");
        tvEndTime.setText((month + 1) + "/" + day + "/" + year + " 23:59:59");
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}