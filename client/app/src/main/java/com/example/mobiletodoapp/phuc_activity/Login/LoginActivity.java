package com.example.mobiletodoapp.phuc_activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.MainActivity;
import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.Logup.LogupActivity;
import com.example.mobiletodoapp.phuc_activity.MainScreenActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView logupDirectText;
    Button loginButton;
    EditText loginEmail, loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logupDirectText = findViewById(R.id.logupDirectText);
        loginButton = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
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
                if (!validateEmail() | !validatePassword()){
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    checkUserExisted();
                }
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });
    }
    public Boolean validateEmail() {
        String value = loginEmail.getText().toString();
        if(value.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }
    public Boolean validatePassword() {
        String value = loginPassword.getText().toString();
        if(value.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }
    public void checkUserExisted() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    loginEmail.setError(null);
                    String passwordFromDb = snapshot.child(email).child("password").getValue(String.class);

                    if(passwordFromDb.equals(password)) {
                        loginEmail.setError(null);

                        String emailFromDb = snapshot.child(email).child("email").getValue(String.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid password");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginEmail.setError("Email does not exist");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}