package com.example.mobiletodoapp.thuyen_services;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;

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
import android.widget.Button;
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
import com.example.mobiletodoapp.phuc_activity.api.TaskGroupApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;
import com.example.mobiletodoapp.thuyen_services.taskgroup_view.TasksGroupView;

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

public class ImportantActivity extends AppCompatActivity {

    List<Task> tasks = new ArrayList<>();
    List<TaskGroup> taskGroups = new ArrayList<>();
    Task backupTask = new Task();
    RecyclerView rcvTaskList;
    ImageButton btnShowAddTaskLayout;
    ImageView btnBackToPrevious;
    Button btnUndo;

    TaskApi taskApi;
    TaskGroupApi taskGroupApi;

    Boolean isShowedDialogFragment = false;

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



    private final TaskAdapter adapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {
            if(isShowedDialogFragment == false) {

            }
        }

        @Override
        public void handleCompleteBtn(Task task) {
            if(isShowedDialogFragment == false) {
                copyTask(backupTask, task);
                task.setCompleted(true);
                showLoading();
                updateTask(task);
                btnUndo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void handleImportantBtn(Task task) {
            if(isShowedDialogFragment == false) {
                copyTask(backupTask, task);
                task.setImportant(false);
                showLoading();
                updateTask(task);
                btnUndo.setVisibility(View.VISIBLE);
            }
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important);

        init();

        showLoading();
        getTaskFromServer();

        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setHasFixedSize(true);
        adapter.setData(tasks);
        rcvTaskList.setAdapter(adapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowedDialogFragment == false) {
                    finish();
                }
            }
        });

        btnShowAddTaskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowedDialogFragment == false) {

                    showLoading();
                    getTaskGroupsFromServer();
                    btnUndo.setVisibility(View.GONE);

                }
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isShowedDialogFragment = true;
                edtTaskTitle.setText("");
                edtDescription.setText("");
                clAddTask.setVisibility(View.GONE);
            }
        });

        btnCancelAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowedDialogFragment = true;
                clAddTask.setVisibility(View.GONE);
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

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickedBtnAddTask();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowedDialogFragment == false) {
                    Log.d("update task", "indo" + backupTask.getId());
                    showLoading();
                    updateTask(backupTask);
                    btnUndo.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init() {
        btnShowAddTaskLayout = findViewById(R.id.btn_show_add_task_layout);
        rcvTaskList = findViewById(R.id.rcv_ipmortant_list);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnUndo = findViewById(R.id.btn_undo);
        btnUndo.setVisibility(View.GONE);

        // add task layotut

        clAddTask = findViewById(R.id.cl_add_task_layout);
        edtTaskTitle = findViewById(R.id.edt_task_title);
        spTasksGroupList = findViewById(R.id.sp_tasksgroup_list);
        tvStartTime = findViewById(R.id.tv_start_time);
        btnPickStartTime = findViewById(R.id.btn_pick_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        btnPickEndTime = findViewById(R.id.btn_pick_end_time);
        edtDescription = findViewById(R.id.edt_description);
        btnCancelAddTask = findViewById(R.id.btn_cancel_add_task);
        btnAddTask = findViewById(R.id.btn_add_task);

        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);
        taskGroupApi = retrofitService.getRetrofit().create(TaskGroupApi.class);

    }

    private CompletableFuture<Void> updateTask(Task task) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Log.d("update task", tasksGroupIdSelected + '\n' +
                task.getTitle() + '\n' +
                task.getDescription() + '\n' +
                task.getStartTime() + '\n' +
                task.getEndTime());

        taskApi.updateTask(task).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()) {
                    getTaskFromServer();
                    adapter.setData(tasks);
                    hideLoading();
                    Log.d("update task", "true");
                } else {
                    Log.d("update task", "false");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("update task", t.toString());
            }
        });

        return future;
    }

    private void copyTask(Task backupTask, Task task) {
        backupTask.setTaskGroupId(task.getTaskGroupId());
        backupTask.setTitle(task.getTitle());
        backupTask.setDescription(task.getDescription());
        backupTask.setStartTime(task.getStartTime());
        backupTask.setEndTime(task.getEndTime());
        backupTask.setImportant(task.isImportant());
        backupTask.setCompleted(task.isCompleted());
        backupTask.setMyDay(task.isMyDay());
        backupTask.setId(task.getId());
        Log.d("copy task", task.getId());
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

    private void handleClickedBtnAddTask() {
        String title = edtTaskTitle.getText().toString().trim();
        String des = edtDescription.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        if (title == null || title.isEmpty()) {
            Toast.makeText(ImportantActivity.this, "Tên nhiệm vụ không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            Task task = new Task(tasksGroupIdSelected, title, des, startTime, endTime);
            task.setImportant(true);
            createTask(taskApi, task);
            edtDescription.setText("");
            edtTaskTitle.setText("");
            clAddTask.setVisibility(View.GONE);
            isShowedDialogFragment = false;
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
                        adapter.setData(tasks);
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
    private void handleShowAddTaskLayout() {
        clAddTask.setVisibility(View.VISIBLE);
        isShowedDialogFragment = true;

        ArrayAdapter<TaskGroup> spinnerAdapter = new ArrayAdapter<TaskGroup>(ImportantActivity.this,
                android.R.layout.simple_spinner_item, taskGroups);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTasksGroupList.setAdapter(spinnerAdapter);
        spTasksGroupList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskGroup taskGroup = (TaskGroup) parent.getSelectedItem();
                tasksGroupIdSelected = taskGroup.getId();
                Log.d("create task", "select id: " + tasksGroupIdSelected);
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

    CompletableFuture<Void> getTaskGroupsFromServer() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String userId = getSharedPref(this, "userId", "default id");
        taskGroupApi.getTasksGroup(userId).enqueue(new Callback<List<TaskGroup>>() {
            @Override
            public void onResponse(Call<List<TaskGroup>> call, Response<List<TaskGroup>> response) {
                if(response.body() != null) {
                    taskGroups = response.body();
                    handleShowAddTaskLayout();
                    hideLoading();

                } else {
                    Log.d("get taskgroup", "false");
                }
            }

            @Override
            public void onFailure(Call<List<TaskGroup>> call, Throwable t) {
                Log.d("get taskgroup", t.toString());
            }
        });

        return future;
    }

    CompletableFuture<Void> getTaskFromServer() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String userId = getSharedPref(this, "userId", "default id");

        taskApi.getImportantTask(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.body() != null) {
                    tasks = response.body();

                    List<Task> filterTask = new ArrayList<>();
                    for(Task t : tasks) {
                        if(!t.isCompleted()) {
                            filterTask.add(t);
                        }
                    }

                    tasks = filterTask;


                    adapter.setData(tasks);
                    hideLoading();
                } else {
                    Log.d("get important task", "failed");
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.d("get important task", t.toString());
            }
        });

        return future;
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