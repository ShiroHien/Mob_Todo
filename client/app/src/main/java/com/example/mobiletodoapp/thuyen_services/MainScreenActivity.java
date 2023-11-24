package com.example.mobiletodoapp.thuyen_services;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskGroupApi;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenActivity extends AppCompatActivity {

    LinearLayout myDay;
    LinearLayout important;
    LinearLayout calendar;
    LinearLayout pomodoro;
    ImageButton btnAddTasksgroup;

    ConstraintLayout layoutAddTasksgroup;

    EditText edtGroupTitle;
    TextView tvCancelAddGroup;
    TextView tvAddGroup;

    RecyclerView recyclerView;



    List<TaskGroup> tasksGroups = new ArrayList<>();
    TaskGroupApi taskGroupApi;

    private ProgressDialog progressDialog;

    private final TasksGroupAdapter adapter = new TasksGroupAdapter(new TasksGroupAdapter.IClickTasksGroupItem() {
        @Override
        public void moveToTaskGroupView(TaskGroup tasksGroup) {
            Intent intent = new Intent(MainScreenActivity.this, TasksGroupView.class);
            startActivity(intent);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        init();

        RetrofitService retrofitService = new RetrofitService();
        taskGroupApi = retrofitService.getRetrofit().create(TaskGroupApi.class);

        getTasksGroupsFromServer(taskGroupApi);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter.setData(tasksGroups);
        recyclerView.setAdapter(adapter);

        btnAddTasksgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutAddTasksgroup.setVisibility(View.VISIBLE);
            }
        });
        tvCancelAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGroupTitle.setText("");
                layoutAddTasksgroup.setVisibility(View.GONE);
            }
        });
        tvAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTasksGroup(edtGroupTitle.getText().toString().trim());
            }
        });

        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, ImportantActivity.class);
                startActivity(intent);
            }
        });

        myDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, MyDayActivity.class);
                startActivity(intent);
            }
        });

        pomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, PomodoroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        myDay = findViewById(R.id.my_day);
        important = findViewById(R.id.important);
        calendar = findViewById(R.id.calendar);
        pomodoro = findViewById(R.id.pomodoro);
        btnAddTasksgroup = findViewById(R.id.btn_add_tasksgroup);
        recyclerView = findViewById(R.id.rcv_tasksgroup);

        layoutAddTasksgroup = findViewById(R.id.layout_add_tasksgroup);
        edtGroupTitle = findViewById(R.id.edt_group_title);
        tvCancelAddGroup = findViewById(R.id.tv_cancel_add_tasksgroup);
        tvAddGroup = findViewById(R.id.tv_add_tasksgroup);




    }

    private void addTasksGroup(String title) {
        if(title == null || title.isEmpty()) {
            Toast.makeText(MainScreenActivity.this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            String userId = getSharedPref(this, "userId", "default id");
            Log.d("pref", userId);
            TaskGroup taskGroup = new TaskGroup(title, userId);
            createTasksGroup(taskGroupApi, taskGroup);

            edtGroupTitle.setText("");
            layoutAddTasksgroup.setVisibility(View.GONE);
        }
    }

    private CompletableFuture<Void> createTasksGroup(TaskGroupApi taskGroupApi, TaskGroup taskGroup) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Log.d("create tasksgroup", "create tasksgroup func");

        taskGroupApi.createTaskGroup(taskGroup).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if(response.body()) {
                        tasksGroups.add(taskGroup);
                        adapter.setData(tasksGroups);
                        Log.d("create tasksgroup", "them nhom thanh cong");
                    } else {
                        Log.d("create tasksgroup", "them nhom that bai");
                    }
                    future.complete(null);
                }catch (Exception e) {
                    Log.d("create tasksgroup", "loi");
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("create tasksgroup", "onFailure");
                future.completeExceptionally(t);
            }
        });

        return  future;
    }

    private CompletableFuture<Void> getTasksGroupsFromServer(TaskGroupApi taskGroupApi) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String userId = getSharedPref(this, "userId", "default id");
        Log.d("pref", userId);

        taskGroupApi.getTasksGroup(userId).enqueue(new Callback<List<TaskGroup>>() {
            @Override
            public void onResponse(Call<List<TaskGroup>> call, Response<List<TaskGroup>> response) {
                try {
                    tasksGroups = response.body();
                    adapter.setData(tasksGroups);
                    Log.d("get data", response.body().toString());
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<List<TaskGroup>> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }
}