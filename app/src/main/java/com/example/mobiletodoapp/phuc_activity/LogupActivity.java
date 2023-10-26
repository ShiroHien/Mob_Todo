package com.example.mobiletodoapp.phuc_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiletodoapp.Helper.Helper;
import com.example.mobiletodoapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogupActivity extends AppCompatActivity {
    EditText full_name, username, email, password;
    TextView loginDirect;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        full_name = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.logup_button);
        loginDirect = findViewById(R.id.login_text);
        loginDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                String name = full_name.getText().toString();
                String userName = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                Helper helper = new Helper(name, userName, Email, Password);
                reference.child(userName).setValue(helper);

                Toast.makeText(LogupActivity.this, "Sign up successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LogupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}