package com.example.mobiletodoapp.trung_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mobiletodoapp.R;

import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    private EditText etTitle;
    private Switch switchFullDay;

    private TextView startTime, endTime;
    private LinearLayout startTimeContainer, endTimeContainer;
    private Button btnAdd;
    int startHour,startMin,endHour,endMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initWidgets();
    }


    private void initWidgets() {
        etTitle = findViewById(R.id.etTitle);
        switchFullDay = findViewById(R.id.switchFullDay);
        startTime = findViewById(R.id.etStartTime);
        endTime = findViewById(R.id.etEndTime);
        startTimeContainer = findViewById(R.id.startTimeContainer);
        endTimeContainer = findViewById(R.id.endTimeContainer);
        btnAdd = findViewById(R.id.btnAddEvent);


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        startHour = h;
                        startMin = m;
                        startTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,startHour,startMin,true);
                timePickerDialog.setTitle("Chọn giở bắt đầu");
                timePickerDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        endHour = h;
                        endMin = m;
                        endTime.setText(String.format(Locale.getDefault(),"%02d:%02d",h,m));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),onTimeSetListener,endHour,endMin,true);
                timePickerDialog.setTitle("Chọn giở kết thúc");
                timePickerDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}