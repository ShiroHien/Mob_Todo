package com.example.timerappclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PomoSettingsActivity extends AppCompatActivity {
    private final static long DEFAULT_WORK_DURATION = 1500000;
    private final static long DEFAULT_BREAK_DURATION = 300000;
    private SeekBar breakSeekBar;
    private SeekBar focusSeekBar;
    private TextView shortBreakTime;
    private TextView focusTime;
    private TextView shortBreakLabel;
    private TextView focusLabel;
    private TextView workDescriptionLabel;
    private TextView breakDescriptionLabel;
    private ConstraintLayout settingsLayout;
    private int breakStatus;
    private int workStatus;
    private int colourPrimary;
    private int colourText;
    private int colourBackground;
    private long newBreakDurationInMillis;
    private long newWorkDurationInMillis;
    private final static int minTimeInMinutes = 1;
    private SharedPreferences savedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomo_settings);

        settingsLayout = findViewById(R.id.configLayout);

        breakSeekBar = findViewById(R.id.shortBreakSeekBar);
        focusSeekBar = findViewById(R.id.focusSeekBar);
        shortBreakLabel = findViewById(R.id.shortBreakText);
        shortBreakTime = findViewById(R.id.shortBreakTime);
        focusLabel = findViewById(R.id.focusTime);
        focusTime = findViewById(R.id.focusTime);

        breakSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        focusSeekBar.setOnSeekBarChangeListener(new SeekBarListener());

        Intent intent = getIntent();

        newBreakDurationInMillis = intent.getLongExtra("setBreakDurationInMillis",
                DEFAULT_BREAK_DURATION);
        newWorkDurationInMillis = intent.getLongExtra("setWorkDurationInMillis",
                DEFAULT_WORK_DURATION);
        breakStatus = convertMillisToMin(newBreakDurationInMillis);
        workStatus = convertMillisToMin(newWorkDurationInMillis);

        savedPrefs = getSharedPreferences( "SettingsPrefs", MODE_PRIVATE );

        updateActivityColourScheme();
        updateCurrentWidgetWithSettings();

    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String statusView;
            if (seekBar.getId() == R.id.shortBreakSeekBar) {
                breakStatus = progress + minTimeInMinutes;
                if (breakStatus == 1) {
                    statusView = breakStatus + " Minute";
                }
                else {
                    statusView = breakStatus + " Minutes";
                }
                shortBreakTime.setText(statusView);
                newBreakDurationInMillis = convertMinToMillis(breakStatus);
            }

            else if (seekBar.getId() == R.id.focusSeekBar) {
                workStatus = progress+minTimeInMinutes;
                if (workStatus == 1) {
                    statusView = workStatus + " Minute";
                }
                else {
                    statusView = workStatus + " Minutes";
                }
                focusTime.setText(statusView);
                newWorkDurationInMillis = convertMinToMillis(workStatus);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }

    // not have back button yet
//    class ButtonListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.timerButton) {
//                exitToTimerActivity();
//            }
//        }
//    }

    public void exitToTimerActivity() {
        Intent intent = new Intent();

        intent.putExtra("newBreakDurationInMillis", newBreakDurationInMillis);
        intent.putExtra("newWorkDurationInMillis", newWorkDurationInMillis);

        setResult(RESULT_OK, intent);

        finish();
    }

    private long convertMinToMillis(int minutes) {
        return (minutes * 60 * 1000);
    }
    private int convertMillisToMin(long millis) {
        return ((int)(millis / 60 / 1000));

    }

    private void updateColourSchemeColour() {
        colourPrimary = getColor(R.color.lightPrimary);
        colourText = getColor(R.color.lightText);
        colourBackground = getColor(R.color.lightBackground);
    }

    private void updateWidgetColourScheme() {
        settingsLayout.setBackgroundColor(colourBackground);
        shortBreakLabel.setTextColor(colourText);
        breakDescriptionLabel.setTextColor(colourText);
        shortBreakTime.setTextColor(colourText);
        focusLabel.setTextColor(colourText);
        focusTime.setTextColor(colourText);
        workDescriptionLabel.setTextColor(colourText);

        breakSeekBar.setProgressTintList(ColorStateList.valueOf(colourPrimary));
        breakSeekBar.setThumbTintList(ColorStateList.valueOf(colourPrimary));
        focusSeekBar.setProgressTintList(ColorStateList.valueOf(colourPrimary));
        focusSeekBar.setThumbTintList(ColorStateList.valueOf(colourPrimary));
    }

    private void updateCurrentWidgetWithSettings() {

        breakSeekBar.setProgress(breakStatus - minTimeInMinutes);
        focusSeekBar.setProgress(workStatus - minTimeInMinutes);
    }

    private void updateActivityColourScheme() {
        updateColourSchemeColour();
        updateWidgetColourScheme();
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor prefsEditor = savedPrefs.edit();
        prefsEditor.putInt("breakStatus", breakStatus);
        prefsEditor.putInt("workStatus", workStatus);
        prefsEditor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        breakStatus = savedPrefs.getInt("breakStatus", 5);
        workStatus = savedPrefs.getInt("workStatus", 25);
    }

    @Override
    public void onBackPressed() {
        exitToTimerActivity();
    }
}
