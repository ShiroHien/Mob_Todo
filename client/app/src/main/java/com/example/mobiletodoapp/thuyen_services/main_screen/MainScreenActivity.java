package com.example.mobiletodoapp.thuyen_services.main_screen;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.setImage;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.EventsApi;
import com.example.mobiletodoapp.phuc_activity.api.TimetableApi;
import com.example.mobiletodoapp.phuc_activity.dto.Timetable;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.example.mobiletodoapp.phuc_activity.view.Setting.SettingActivity;
import com.example.mobiletodoapp.thuyen_services.ImportantActivity;
import com.example.mobiletodoapp.thuyen_services.MyDayActivity;
import com.example.mobiletodoapp.thuyen_services.PomodoroActivity;
import com.example.mobiletodoapp.thuyen_services.TasksGroupAdapter;
import com.example.mobiletodoapp.thuyen_services.taskgroup_view.TasksGroupView;
import com.example.mobiletodoapp.trung_activity.CalendarUtils;
import com.example.mobiletodoapp.trung_activity.MonthViewActivity;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskGroupApi;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.time.LocalDate;
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
    LinearLayout btnAddTasksgroup;

    ConstraintLayout layoutAddTasksgroup;

    EditText edtGroupTitle;
    TextView tvCancelAddGroup;
    TextView tvAddGroup;

    RecyclerView recyclerView;

    List<TaskGroup> tasksGroups = new ArrayList<>();
    TaskGroupApi taskGroupApi;
    TextView username, email;
    ImageView ava;
    private ProgressDialog progressDialog;

    Boolean isShowedDialogFragment = false;

    private final TasksGroupAdapter adapter = new TasksGroupAdapter(new TasksGroupAdapter.IClickTasksGroupItem() {
        @Override
        public void moveToTaskGroupView(TaskGroup tasksGroup) {
            if(isShowedDialogFragment == false) {
                Intent intent = new Intent(MainScreenActivity.this, TasksGroupView.class);
                intent.putExtra("tasksgroupId", tasksGroup.getId());
                intent.putExtra("tasksgroupTitle", tasksGroup.getTitle());
                startActivity(intent);
            }

//            Toast.makeText(MainScreenActivity.this, tasksGroup.getId(), Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        init();

        RetrofitService retrofitService = new RetrofitService();
        taskGroupApi = retrofitService.getRetrofit().create(TaskGroupApi.class);
        CalendarUtils.timetableApi = retrofitService.getRetrofit().create(TimetableApi.class);
        CalendarUtils.eventsApi = retrofitService.getRetrofit().create(EventsApi.class);
        String userId = getSharedPref(this, "userId", "");
        Log.d("Calendar", "userId: " + userId);
        CalendarUtils.loadTimetableForUser(this, userId);

        showLoading();
        getTasksGroupsFromServer(taskGroupApi);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter.setData(tasksGroups);
        recyclerView.setAdapter(adapter);


        btnAddTasksgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutAddTasksgroup.setVisibility(View.VISIBLE);
//                CalendarUtils.fadeInAnimation(layoutAddTasksgroup,300);
                CalendarUtils.fadeInAnimation(layoutAddTasksgroup,300);
                isShowedDialogFragment = true;

            }
        });

        ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    Intent intent = new Intent(MainScreenActivity.this, ImportantActivity.class);
                    startActivity(intent);
                }
            }
        });

        myDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    Intent intent = new Intent(MainScreenActivity.this, MyDayActivity.class);
                    startActivity(intent);
                }
            }
        });

        pomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowedDialogFragment == false) {
                    Intent intent = new Intent(MainScreenActivity.this, PomodoroActivity.class);
                    startActivity(intent);
                }
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowedDialogFragment == false) {
                    Intent intent = new Intent(getApplicationContext(), MonthViewActivity.class);
                    startActivity(intent);
                }
            }
        });


        tvCancelAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGroupTitle.setText("");
                layoutAddTasksgroup.setVisibility(View.GONE);
                isShowedDialogFragment = false;
            }
        });
        tvAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTasksGroup(edtGroupTitle.getText().toString().trim());
            }
        });

    }


    private void init() {
        CalendarUtils.selectedDate = LocalDate.now();
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
        username = findViewById(R.id.user_name);
        email = findViewById(R.id.gmail);
        username.setText(getSharedPref(MainScreenActivity.this, "username", ""));
        email.setText(getSharedPref(MainScreenActivity.this, "email", ""));
        ava = findViewById(R.id.ava_user);
        setImage(MainScreenActivity.this, getSharedPref(MainScreenActivity.this, "ava", ""), ava);
    }

    private void addTasksGroup(String title) {
        if (title == null || title.isEmpty()) {
            Toast.makeText(MainScreenActivity.this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            String userId = getSharedPref(this, "userId", "default id");
            Log.d("pref", userId);
            TaskGroup taskGroup = new TaskGroup(title, userId);
            showLoading();
            createTasksGroup(taskGroupApi, taskGroup);

            edtGroupTitle.setText("");
            layoutAddTasksgroup.setVisibility(View.GONE);
            isShowedDialogFragment = false;
        }
    }

    private CompletableFuture<Void> createTasksGroup(TaskGroupApi taskGroupApi, TaskGroup taskGroup) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Log.d("create tasksgroup", "create tasksgroup func");

        taskGroupApi.createTaskGroup(taskGroup).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.body()) {
                        getTasksGroupsFromServer(taskGroupApi);
                        adapter.setData(tasksGroups);
                        hideLoading();
                        Log.d("create tasksgroup", "them nhom thanh cong");
                    } else {
                        Log.d("create tasksgroup", "them nhom that bai");
                    }
                    future.complete(null);
                } catch (Exception e) {
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

        return future;
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
                    hideLoading();
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

    @Override
    protected void onResume() {
        super.onResume();
        getTasksGroupsFromServer(taskGroupApi);
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