package com.example.mobiletodoapp.phuc_activity.view.Setting;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.clearSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.saveSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.setImage;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TaskApi;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;
import com.example.mobiletodoapp.phuc_activity.dto.UpdateUser;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.example.mobiletodoapp.phuc_activity.view.TaskDetail.TaskDetailActivity;
import com.example.mobiletodoapp.thuyen_services.main_screen.MainScreenActivity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    ImageView btnBackToPrevio, ava_user;
    EditText username, name;
    Button logout;
    TextView user_name, gmail, text_save;
    private UserApi userApi;
    private RetrofitService retrofitService;
    Boolean isShowTextSaved = false;
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
        setContentView(R.layout.activity_setting);
        init();
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
                String userId = getSharedPref(SettingActivity.this, "userId", "");
                callUpdateUser(userId);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedUsername", s.toString());
                setResult(RESULT_OK, resultIntent);
            }
        };
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPref(SettingActivity.this);
                showToast(SettingActivity.this, "Đăng xuất thành công");
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnBackToPrevio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username.addTextChangedListener(textWatcher);
        name.addTextChangedListener(textWatcher);
    }
    private void init() {
        btnBackToPrevio = findViewById(R.id.btn_back_to_previous);
        ava_user = findViewById(R.id.ava_user);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        user_name = findViewById(R.id.user_name);
        gmail = findViewById(R.id.gmail);
        logout = findViewById(R.id.logout);
        user_name.setText(getSharedPref(SettingActivity.this, "username", ""));
        gmail.setText(getSharedPref(SettingActivity.this, "email", ""));
        username.setText(getSharedPref(SettingActivity.this, "username", ""));
        name.setText(getSharedPref(SettingActivity.this, "name", ""));
        setImage(SettingActivity.this, getSharedPref(SettingActivity.this, "ava", ""), ava_user);
        text_save = findViewById(R.id.text_save);

        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);
    }
    private CompletableFuture<Void> updateUser(UserApi userApi, String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        UpdateUser updateUser = new UpdateUser(username.getText().toString(), name.getText().toString());
        userApi.updateUser(updateUser, userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    System.out.println(response.body());
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result == true) {
                            saveSharedPref(SettingActivity.this, "username", username.getText().toString());
                            saveSharedPref(SettingActivity.this, "name", name.getText().toString());
                            if (isShowTextSaved == true) {
                                isShowTextSaved = false;
                                text_save.setVisibility(View.GONE);
                            }
                        } else {

                        }
                    } else {
                        showToast(SettingActivity.this, "Cập nhập không thành công");
                    }
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("error", "loi gi do", t);
                showToast(SettingActivity.this, "Có lỗi xảy ra");
                future.completeExceptionally(t);
            }
        });
        return future;
    }
    private void callUpdateUser(String userId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> completableFuture = new CompletableFuture<>();

                Future<?> future = executorService.submit(() -> {
                    try {
                        updateUser(userApi, userId);
                        completableFuture.complete(null);
                    } catch (Exception e) {
                        completableFuture.completeExceptionally(e);
                    }
                });

                try {
                    future.get(2000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    showToast(SettingActivity.this, "Timeout khi gọi API");
                }

                executorService.shutdown();
            }
        } catch (Exception e) {
            showToast(SettingActivity.this, "Có lỗi xảy ra");
        }
    }
}