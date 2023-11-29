package com.example.mobiletodoapp.phuc_activity.view.Setting;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.clearSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.setImage;
import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.example.mobiletodoapp.thuyen_services.main_screen.MainScreenActivity;

public class SettingActivity extends AppCompatActivity {
    ImageView btnBackToPrevio, ava_user;
    EditText username, name;
    Button logout;
    TextView user_name, gmail;
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
    }
}