package com.example.mobiletodoapp.thuyen_services;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.dto.Task;
import com.example.mobiletodoapp.phuc_activity.dto.TaskGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportantActivity extends AppCompatActivity {

    List<Task> tasks = new ArrayList<>();
    Task backupTask;
    RecyclerView rcvTaskList;
    ImageButton btnAddTask;
    ImageView btnBackToPrevious;
    Button btnUndo;

    TaskApi taskApi;

    Boolean isShowedDialogfragment = false;

    ProgressDialog progressDialog;

    private final TaskAdapter adapter = new TaskAdapter(new TaskAdapter.IClickTaskItem() {
        @Override
        public void moveToTaskView(Task task) {
            if(isShowedDialogfragment == false) {

            }
        }

        @Override
        public void handleCompleteBtn(Task task) {
            if(isShowedDialogfragment == false) {
                tasks.remove(task);
                adapter.setData(tasks);
                btnUndo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void handleImportantBtn(Task task) {
            if(isShowedDialogfragment == false) {
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
                if(isShowedDialogfragment == false) {
                    finish();
                }
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowedDialogfragment == false) {

                }
            }
        });
    }

    private void init() {
        btnAddTask = findViewById(R.id.btn_add_task);
        rcvTaskList = findViewById(R.id.rcv_ipmortant_list);
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnUndo = findViewById(R.id.btn_undo);

        taskApi = new RetrofitService().getRetrofit().create(TaskApi.class);


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