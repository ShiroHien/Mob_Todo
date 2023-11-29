package com.example.mobiletodoapp.thuyen_services.main_screen;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;

public class PlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = getSharedPref(PlashActivity.this, "userId", "");
                Intent intent;
                if(userId == "") {
                     intent = new Intent(PlashActivity.this, LoginActivity.class);
                } else {
                     intent = new Intent(PlashActivity.this, MainScreenActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}