package com.example.mobiletodoapp.phuc_activity.view.Logup;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.validateEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.MainScreenActivity;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;
import com.example.mobiletodoapp.phuc_activity.dto.User;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogupActivity extends AppCompatActivity {
    EditText full_name, username, email, password;
    TextView loginDirect;
    Button signupButton;
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
        setContentView(R.layout.activity_logup);

        initialize();
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        loginDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index of the drawable right of the EditText

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        if (isPasswordVisible) {
                            // Password is currently visible, hide it
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            // Password is currently hidden, show it
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        isPasswordVisible = !isPasswordVisible; // Toggle the visibility flag
                        return true;
                    }
                }
                return false;
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = full_name.getText().toString();
                String userName = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (!validateEmail(Email) || !validatePassword(Password) || !validateEmpty(name, "Tên không được để trống", LogupActivity.this) || !validateEmpty(userName, "Tên người dùng không được để trống", LogupActivity.this)) {

                } else {
                    logupUser(userApi, name, userName, Email, Password);
                }
            }
        });
    }

    private Boolean validateEmail(String value) {
        if (value.isEmpty()) {
            showToast(LogupActivity.this, "Email không được để trống");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            showToast(LogupActivity.this, "Email không hợp lệ");
            return false;
        } else {
            return true;
        }
    }

    private Boolean validatePassword(String value) {
        if (value.isEmpty()) {
            showToast(LogupActivity.this, "Mật khẩu không được để trống");
            return false;
        } else if (value.length() < 6) {
            showToast(LogupActivity.this, "Password must be at least 6 characters long");
            return false;
        } else if (!containsLetterAndNumber(value)) {
            showToast(LogupActivity.this, "Password must contain both letters and numbers");
            return false;
        } else {
            return true;
        }
    }

    private boolean containsLetterAndNumber(String value) {
        boolean containsLetter = false;
        boolean containsNumber = false;

        for (char c : value.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsNumber = true;
            }

            if (containsLetter && containsNumber) {
                return true;
            }
        }

        return false;
    }

    private void initialize() {
        full_name = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.logup_button);
        loginDirect = findViewById(R.id.login_text);
    }

    private void logupUser(UserApi userApi, String name, String userName, String Email, String Password) {
        User user = new User(name, userName, Email, Password);
        userApi.saveUser(user).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean logupResult = response.body();
                    if (logupResult != null && logupResult) {
                        showToast(LogupActivity.this, "Đăng ký thành công");
                        Intent intent = new Intent(LogupActivity.this, MainScreenActivity.class);
                        startActivity(intent);
                    } else {
                        showToast(LogupActivity.this, "Đăng ký không thành công. Tài khoản email đã tồn tại");
                    }
                } else {
                    showToast(LogupActivity.this, "Đăng ký không thành công");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showToast(LogupActivity.this, "Có lỗi xảy ra");
            }
        });
    }
}