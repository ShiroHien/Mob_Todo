package com.example.mobiletodoapp.phuc_activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.MainActivity;
import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.Logup.LogupActivity;
import com.example.mobiletodoapp.phuc_activity.MainScreenActivity;
import com.example.mobiletodoapp.phuc_activity.model.Login;
import com.example.mobiletodoapp.phuc_activity.retrofit.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.retrofit.UserApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView logupDirectText;
    Button loginButton;
    EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        logupDirectText = findViewById(R.id.logupDirectText);
        loginButton = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
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
                if (!validateEmail() | !validatePassword()) {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(userApi);
                }
//                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
//                startActivity(intent);
            }
        });
    }

    public Boolean validateEmail() {
        String value = loginEmail.getText().toString();
        if (value.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String value = loginPassword.getText().toString();
        if (value.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    private void loginUser(UserApi userApi) {
        Login login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString());
        userApi.loginUser(login)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        System.out.println("result: "+call);
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
                    }
                });
    }
}