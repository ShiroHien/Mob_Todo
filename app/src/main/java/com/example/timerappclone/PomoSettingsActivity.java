package com.example.aipomo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PomoSettingsActivity extends AppCompatActivity {

    private EditText workDurationEditText, breakDurationEditText;
    private Button saveSettingsButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomo_settings);

        // Initialize views
        workDurationEditText = findViewById(R.id.workDurationEditText);
        breakDurationEditText = findViewById(R.id.breakDurationEditText);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        // Load saved settings
        sharedPreferences = getSharedPreferences("PomodoroSettings", MODE_PRIVATE);
        workDurationEditText.setText(sharedPreferences.getString("workDuration", "25"));
        breakDurationEditText.setText(sharedPreferences.getString("breakDuration", "5"));

        // Save button click listener
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workDuration", workDurationEditText.getText().toString());
        editor.putString("breakDuration", breakDurationEditText.getText().toString());
        editor.apply();

        // Optionally, you can display a message to the user indicating that the settings have been saved
        // For now, simply finish the activity to return to the main screen
        finish();
    }
}
