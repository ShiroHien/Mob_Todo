package com.mob.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.todoapp.MainActivity;
import com.mob.todoapp.R;

public class UpdateTasksGroupAcitivity extends AppCompatActivity {
    
    Toolbar toolbar;

    EditText edtNewTitle;
    Button btnUpdateTasksGroup;
    
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tasks_group);
    
        toolbar = findViewById(R.id.tb_update_tasksgroup);
        edtNewTitle = findViewById(R.id.edt_newTitle);
        btnUpdateTasksGroup = findViewById(R.id.btn_update_tasksgroup);
        
        intent = getIntent();
        String title = intent.getStringExtra(MainActivity.TASKSGROUP_TITLE);
        toolbar.setTitle(title);
        
        btnUpdateTasksGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    
    private void update() {
        String newTitle = edtNewTitle.getText().toString().trim();
        if(newTitle == "") {
            Toast.makeText(this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            Intent intentResult = new Intent();
            intentResult.putExtra(MainActivity.TASKSGROUP_TITLE, newTitle);
            setResult(Activity.RESULT_OK, intentResult);
            finish();
        }
    }
}