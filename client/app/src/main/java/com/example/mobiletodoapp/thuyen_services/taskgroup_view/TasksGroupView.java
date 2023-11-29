package com.example.mobiletodoapp.thuyen_services.taskgroup_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.MenuItem;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.api.TaskGroupApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;
import com.example.mobiletodoapp.phuc_activity.view.TaskDetail.TaskDetailActivity;
import com.example.mobiletodoapp.thuyen_services.TaskAdapter;

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

public class TasksGroupView extends AppCompatActivity {

    RecyclerView rcvTaskList;
    ImageButton btnShowAddTaskLayout;
    ImageView btnBackToPrevious;

    TextView tvHeaderTitlte;

    List<Task> tasks = new ArrayList<>();

    Intent intent;

    TaskApi taskApi;
    TaskGroupApi taskGroupApi;

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

    ImageView btnShowPopupMenu;
    TaskGroup mTaskGroup;

    ConstraintLayout clUpdateTaskGroup;
    EditText edtNewGroupTitle;
    TextView btnCancelUpdateTaskGroup;
    TextView btnUpdateTaskGroup;

    Boolean isShowedDialogFragment = false;


    private final TaskAdapter taskAdapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {
            if (isShowedDialogFragment == false) {
                Intent taskDetailIntent = new Intent(TasksGroupView.this, TaskDetailActivity.class);
                taskDetailIntent.putExtra("taskId", task.getId());
                taskDetailIntent.putExtra("taskTitle", task.getTitle());
                taskDetailIntent.putExtra("taskgroupTitle", intent.getStringExtra("tasksgroupTitle"));
                startActivity(taskDetailIntent);
            }
        }

        @Override
        public void handleCompleteBtn(Task task) {
            if (isShowedDialogFragment == false) {
                if (task.isCompleted()) {
                    task.setCompleted(false);
                } else {
                    task.setCompleted(true);
                }
                showLoading();
                updateTask(task);
            }
        }

        @Override
        public void handleImportantBtn(Task task) {
            if (isShowedDialogFragment == false) {
                if (task.isImportant()) {
                    task.setImportant(false);
                } else {
                    task.setImportant(true);
                }
                showLoading();
                updateTask(task);
            }
        }
    });

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
        setContentView(R.layout.activity_tasks_group_view);

        init();
        taskApi = new RetrofitService().getRetrofit().create(TaskApi.class);
        taskGroupApi = new RetrofitService().getRetrofit().create(TaskGroupApi.class);
        showLoading();
        getTaskListFromServer(taskApi);

        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setHasFixedSize(true);
        taskAdapter.setData(tasks);
        rcvTaskList.setAdapter(taskAdapter);

        btnBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    finish();
                }
            }
        });

        btnShowAddTaskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    isShowedDialogFragment = true;
                    handleShowAddTaskLayout();
                }
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
                isShowedDialogFragment = false;
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickedBtnAddTask();
            }
        });

        btnShowPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    showPopupMenu();
                }
            }
        });

        btnCancelUpdateTaskGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clUpdateTaskGroup.setVisibility(View.GONE);
                edtNewGroupTitle.setText("");
            }
        });

        btnUpdateTaskGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskGroup();
            }
        });

    }

    private void init() {

        // layout taskgroup view
        rcvTaskList = findViewById(R.id.rcv_task_list);
        btnShowAddTaskLayout = findViewById(R.id.btn_show_add_task_layout);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        tvHeaderTitlte = findViewById(R.id.tv_header_title);


        // layout add task
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

        // show popup menu
        btnShowPopupMenu = findViewById(R.id.btn_show_popup_menu);

        // layout update taskgroup
        clUpdateTaskGroup = findViewById(R.id.inc_update_taskgroup_layout);
        edtNewGroupTitle = findViewById(R.id.edt_new_group_title);
        btnUpdateTaskGroup = findViewById(R.id.tv_update_tasksgroup);
        btnCancelUpdateTaskGroup = findViewById(R.id.tv_cancel_update_tasksgroup);


        intent = this.getIntent();

        tvHeaderTitlte.setText(intent.getStringExtra("tasksgroupTitle"));

    }

    private void updateTaskGroup() {
        String newTitle = edtNewGroupTitle.getText().toString().trim();
        if (newTitle == null || newTitle.isEmpty()) {
            Toast.makeText(TasksGroupView.this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            showLoading();
            getTaskGroupById(taskGroupApi, intent.getStringExtra("tasksgroupId"));
            clUpdateTaskGroup.setVisibility(View.GONE);
            isShowedDialogFragment = false;
        }
    }


    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnShowPopupMenu);

        popupMenu.getMenuInflater().inflate(R.menu.tasksgroup_view_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.update_title) {
                    clUpdateTaskGroup.setVisibility(View.VISIBLE);
                    isShowedDialogFragment = true;
                    return true;
                } else if (itemId == R.id.delete_tasksgroup) {
                    showLoading();
                    deleteTaskGroupById();
                    return true;
                }
                return false;

            }


        });

        popupMenu.show();
    }

    private CompletableFuture<Void> updateTask(Task task) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        taskApi.updateTask(task).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    taskAdapter.setData(tasks);
                    Log.d("update task", task.getTitle() + " true");
                    hideLoading();
                } else {
                    Log.d("update task", "true");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("update task", t.toString());
            }
        });

        return future;
    }

    private CompletableFuture<Void> deleteTaskGroupById() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String taskGroupId = intent.getStringExtra("tasksgroupId");
        taskGroupApi.delateTaskGroup(taskGroupId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    hideLoading();
                    finish();
                } else {
                    Log.d("delete taskgroup", "false");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("delete taskgroup", t.toString());
            }
        });
        return future;
    }

    private CompletableFuture<Void> getTaskGroupById(TaskGroupApi taskGroupApi, String taskgroupId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        taskGroupApi.getTaskGroupById(taskgroupId).enqueue(new Callback<TaskGroup>() {
            @Override
            public void onResponse(Call<TaskGroup> call, Response<TaskGroup> response) {
                if (response.body() != null) {
                    mTaskGroup = response.body();
                    mTaskGroup.setTitle(edtNewGroupTitle.getText().toString().trim());
                    updateTaskGroup(taskGroupApi);
//                    Log.d("get taskgroup", mTaskGroup.getId());
//                    hideLoading();
                } else {
                    Log.d("get taskgroup", "failed");
                }
            }

            @Override
            public void onFailure(Call<TaskGroup> call, Throwable t) {
                Log.d("get taskgroup", "err connection");
                Log.d("get taskgroup", t.toString());
            }
        });
        return future;
    }

    private CompletableFuture<Void> updateTaskGroup(TaskGroupApi taskGroupApi) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Log.d("update taskgroup", mTaskGroup.getId());
        taskGroupApi.updateTaskGroup(mTaskGroup).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null && response.body() == true) {
                    tvHeaderTitlte.setText(mTaskGroup.getTitle());
                    hideLoading();
                } else {
                    Log.d("update taskgroup", "failed");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("update taskgroup", "err connection");
                Log.d("update taskgroup", t.toString());
            }
        });

        return future;
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
                        getTaskListFromServer(taskApi);
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
        isShowedDialogFragment = true;


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

    @Override
    protected void onResume() {
        super.onResume();
        getTaskListFromServer(taskApi);
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đồng bộ dữ liệu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}