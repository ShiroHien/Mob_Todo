package com.example.timerappclone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private ImageButton startButton, pauseResumeButton, resetButton;
    private ImageView settingsIcon;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomo_main);

        // Initialize views
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        pauseResumeButton = findViewById(R.id.pauseResumeButton);
        resetButton = findViewById(R.id.resetButton);
        settingsIcon = findViewById(R.id.settingsIcon);

        // Set default timer text
        updateTimerText();

        // Set button click listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                startButton.setVisibility(View.GONE);
                pauseResumeButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        });

        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    pauseTimer();
                    pauseResumeButton.setBackgroundResource(R.drawable.ic_start);
                } else {
                    resumeTimer();
                    pauseResumeButton.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                startButton.setVisibility(View.VISIBLE);
                pauseResumeButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
            }
        });

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to settings activity
                Intent intent = new Intent(MainActivity.this, PomoSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                vibrateDevice();
            }
        }.start();
        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (countDownTimer != null && isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    private void resumeTimer() {
        startTimer();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 25 * 60 * 1000;
        updateTimerText();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void vibrateDevice() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                // For devices below API 26, use the old method
                vibrator.vibrate(1000);
            }
        }
    }


}
