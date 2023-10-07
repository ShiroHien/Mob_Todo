package com.mob.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mob.todoapp.MainActivity;
import com.mob.todoapp.R;

public class AddTasksGroupActivity extends AppCompatActivity {



    EditText edtGroupName;
    Button btnAddGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks_group);

        edtGroupName = findViewById(R.id.edt_group_name);
        btnAddGroup = findViewById(R.id.btn_add_group);

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup();
            }
        });
    }

    private void addGroup() {
        String groupName = edtGroupName.getText().toString().trim();
        Intent intent = new Intent();
        if(groupName != "") {
            intent.putExtra(MainActivity.TASKSGROUP_NAME, groupName);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}