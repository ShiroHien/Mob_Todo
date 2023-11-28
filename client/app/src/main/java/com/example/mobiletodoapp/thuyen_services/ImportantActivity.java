package com.example.mobiletodoapp.thuyen_services;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.api.TaskGroupApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;
import com.example.mobiletodoapp.thuyen_services.taskgroup_view.TasksGroupView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportantActivity extends AppCompatActivity {

    List<Task> tasks = new ArrayList<>();
    List<TaskGroup> taskGroups = new ArrayList<>();
    Task backupTask;
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
                tasks.remove(task);
                adapter.setData(tasks);
                btnUndo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void handleImportantBtn(Task task) {
            if(isShowedDialogFragment == false) {
                tasks.remove(task);
                adapter.setData(tasks);
                Toast.makeText(ImportantActivity.this, "Đã xóa nhiệm vụ quan trọng!", Toast.LENGTH_SHORT).show();
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
        getDataFromServer();

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



      

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowedDialogFragment == false) {

                }
            }
        });
    }

    private void init() {
        btnShowAddTaskLayout = findViewById(R.id.btn_show_add_task_layout);
        rcvTaskList = findViewById(R.id.rcv_ipmortant_list);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnUndo = findViewById(R.id.btn_undo);

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

    CompletableFuture<Void> getDataFromServer() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String userId = getSharedPref(this, "userId", "default id");

        taskApi.getImportantTask(userId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.body() != null) {
                    tasks = response.body();
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