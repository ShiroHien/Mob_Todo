package com.example.mobiletodoapp.phuc_activity.view.Login;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.saveSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.validateEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.User;
import com.example.mobiletodoapp.phuc_activity.view.Logup.LogupActivity;
import com.example.mobiletodoapp.phuc_activity.dto.Login;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;
import com.example.mobiletodoapp.thuyen_services.MainScreenActivity;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView logupDirectText;
    Button loginButton;
    EditText loginEmail, loginPassword;
    private boolean isPasswordVisible = false;
    private ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_login);

        initialize();
        checkLoginWithCondition();

        //Tạo retrofit
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        logupDirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LogupActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmpty(loginEmail.getText().toString(), "Email không được để trống", LoginActivity.this) || !validateEmpty(loginPassword.getText().toString(), "Mật khẩu không được để trống", LoginActivity.this)) {
                } else {
                    showLoading();
                    loginUser(userApi);
                }
            }
        });
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index of the drawable right of the EditText

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        if (isPasswordVisible) {
                            // Password is currently visible, hide it
                            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            // Password is currently hidden, show it
                            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        isPasswordVisible = !isPasswordVisible; // Toggle the visibility flag
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initialize() {
        logupDirectText = findViewById(R.id.logupDirectText);
        loginButton = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
    }

    private CompletableFuture<Void> loginUser(UserApi userApi) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Login login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString());
        userApi.loginUser(login).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    processLoginResponse(response);
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoading();
                showToast(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại đường truyền của bạn.");
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    private void processLoginResponse(Response<User> response) {
        if (response.isSuccessful()) {
            User loginResult = response.body();
            if (loginResult != null) {
                saveSharedPref(LoginActivity.this, "userId", loginResult.getId());
                saveSharedPref(LoginActivity.this, "email", loginResult.getEmail());
                saveSharedPref(LoginActivity.this, "name", loginResult.getName());
                saveSharedPref(LoginActivity.this, "username", loginResult.getUsername());
                showToast(LoginActivity.this, "Đăng nhập thành công");
                hideLoading();
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                showToast(LoginActivity.this, "Sai email hoặc mật khẩu");
            }
        } else {
            showToast(LoginActivity.this, "Lỗi server");
        }
    }
    private void checkLoginWithCondition() {
        String savedEmail = getSharedPref(LoginActivity.this, "email", "");
        String userId = getSharedPref(LoginActivity.this, "userId", "");
        String name = getSharedPref(LoginActivity.this, "name", "");
        String username = getSharedPref(LoginActivity.this, "username", "");
        if (!savedEmail.isEmpty() && !username.isEmpty() && !userId.isEmpty() && !name.isEmpty()) {
            showToast(LoginActivity.this, "Tự động đăng nhập thành công");
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        }
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