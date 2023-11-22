package com.example.mobiletodoapp.phuc_activity.view.Login;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.saveSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.validateEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.dto.User;
import com.example.mobiletodoapp.phuc_activity.view.Logup.LogupActivity;
import com.example.mobiletodoapp.phuc_activity.MainScreenActivity;
import com.example.mobiletodoapp.phuc_activity.dto.Login;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;

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

    private void loginUser(UserApi userApi) {
        Login login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString());
        userApi.loginUser(login).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User loginResult = response.body();
                    System.out.println(loginResult);
                    if (loginResult != null) {
                        showToast(LoginActivity.this, "Đăng nhập thành công");
                        saveSharedPref(LoginActivity.this, "email", loginEmail.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                        startActivity(intent);
                    } else {
                        showToast(LoginActivity.this, "Sai email hoặc mật khẩu");
                    }
                } else {
                    showToast(LoginActivity.this, "Lỗi server");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại đường truyền của bạn.");
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
            }
        });
    }
    private void checkLoginWithCondition() {
        String savedEmail = getSharedPref(LoginActivity.this, "email", "");
        if (savedEmail != null && savedEmail != "") {
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
//            finish();
        }
    }
}