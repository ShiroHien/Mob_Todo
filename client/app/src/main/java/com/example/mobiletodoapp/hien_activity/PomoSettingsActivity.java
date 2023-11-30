package com.example.mobiletodoapp.hien_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mobiletodoapp.R;

public class PomoSettingsActivity extends AppCompatActivity {
    private final static long DEFAULT_FOCUS_DURATION = 1500000;
    private final static long DEFAULT_SHORTBREAK_DURATION = 300000;
    private final static long DEFAULT_LONGBREAK_DURATION = 900000;
    private final static int DEFAULT_SESSION = 4;
    private SeekBar focusSeekBar, shortBreakSeekBar, longBreakSeekBar, sessionsSeekBar;
    private TextView focusTime, shortBreakTime, longBreakTime, sessionTimes;
    private int focusStatus, shortBreakStatus, longBreakStatus, sessionStatus;
    private ConstraintLayout settingsLayout;
    private long newFocusDurationInMillis, newShortBreakDurationInMillis,
            newLongBreakDurationInMillis, newSessionSetting;
    private final static int minTimeInMinutes = 1;
    private SharedPreferences savedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomo_settings);
        settingsLayout = findViewById(R.id.configLayout);

        focusSeekBar = findViewById(R.id.focusSeekBar);
        shortBreakSeekBar = findViewById(R.id.shortBreakSeekBar);
        longBreakSeekBar = findViewById(R.id.longBreakSeekBar);
        sessionsSeekBar = findViewById(R.id.sessionsSeekBar);

        focusTime = findViewById(R.id.focusTime);
        shortBreakTime = findViewById(R.id.shortBreakTime);
        longBreakTime = findViewById(R.id.longBreakTime);
        sessionTimes = findViewById(R.id.sessionTimes);

        focusSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        shortBreakSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        longBreakSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        sessionsSeekBar.setOnSeekBarChangeListener(new SeekBarListener());

        Intent intent = getIntent();

        newShortBreakDurationInMillis = intent.getLongExtra("setBreakDurationInMillis", DEFAULT_SHORTBREAK_DURATION);
        newFocusDurationInMillis = intent.getLongExtra("setWorkDurationInMillis", DEFAULT_FOCUS_DURATION);
        newLongBreakDurationInMillis = intent.getLongExtra("setLongBreakDurationInMillis", DEFAULT_LONGBREAK_DURATION);
        newSessionSetting = intent.getLongExtra("setSessionSetting", DEFAULT_SESSION);

        shortBreakStatus = convertMillisToMin(newShortBreakDurationInMillis);
        focusStatus = convertMillisToMin(newFocusDurationInMillis);
        longBreakStatus = convertMillisToMin(newLongBreakDurationInMillis);

        savedPrefs = getSharedPreferences( "SettingsPrefs", MODE_PRIVATE );
        updateCurrentWidgetWithSettings();

        ImageView backButton = findViewById(R.id.btn_back_to_previous);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitToTimerActivity();
            }
        });
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int id = seekBar.getId();

            if (id == R.id.shortBreakSeekBar) {
                shortBreakStatus = progress + minTimeInMinutes;
                updateStatusView(shortBreakStatus, shortBreakTime, "Minute", "Minutes");
                newShortBreakDurationInMillis = convertMinToMillis(shortBreakStatus);
            } else if (id == R.id.focusSeekBar) {
                focusStatus = progress + minTimeInMinutes;
                updateStatusView(focusStatus, focusTime, "Minute", "Minutes");
                newFocusDurationInMillis = convertMinToMillis(focusStatus);
            } else if (id == R.id.longBreakSeekBar) {
                longBreakStatus = progress + minTimeInMinutes;
                updateStatusView(longBreakStatus, longBreakTime, "Minute", "Minutes");
            } else if (id == R.id.sessionsSeekBar) {
                sessionStatus = progress + 1;
                updateStatusView(sessionStatus, sessionTimes, "Session", "Sessions");
            }
        }

        private void updateStatusView(int status, TextView textView, String singular, String plural) {
            String statusView = status + (status == 1 ? " " + singular : " " + plural);
            textView.setText(statusView);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }

    public void exitToTimerActivity() {
        Intent intent = new Intent();

        intent.putExtra("newShortBreakDurationInMillis", newShortBreakDurationInMillis);
        intent.putExtra("newFocusDurationInMillis", newFocusDurationInMillis);
        intent.putExtra("newLongBreakDurationInMillis", newLongBreakDurationInMillis);
        intent.putExtra("newSessionSetting", newSessionSetting);

        setResult(RESULT_OK, intent);

        finish();
    }

    private long convertMinToMillis(int minutes) {
        return (minutes * 60 * 1000);
    }
    private int convertMillisToMin(long millis) {
        return ((int)(millis / 60 / 1000));
    }

    private void updateCurrentWidgetWithSettings() {
        shortBreakSeekBar.setProgress(shortBreakStatus - minTimeInMinutes);
        focusSeekBar.setProgress(focusStatus - minTimeInMinutes);
        longBreakSeekBar.setProgress(longBreakStatus - minTimeInMinutes);
        sessionsSeekBar.setProgress(sessionStatus - 1);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor prefsEditor = savedPrefs.edit();
        prefsEditor.putInt("shortBreakStatus", shortBreakStatus);
        prefsEditor.putInt("focusStatus", focusStatus);
        prefsEditor.putInt("longBreakStatus", longBreakStatus);
        prefsEditor.putInt("sessionStatus", sessionStatus);
        prefsEditor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        shortBreakStatus = savedPrefs.getInt("shortBreakStatus", 5);
        focusStatus = savedPrefs.getInt("focusStatus", 25);
        longBreakStatus = savedPrefs.getInt("longBreakStatus", 15);
        sessionStatus = savedPrefs.getInt("sessionStatus", 4);
    }

    @Override
    public void onBackPressed() {
        exitToTimerActivity();
    }
}
