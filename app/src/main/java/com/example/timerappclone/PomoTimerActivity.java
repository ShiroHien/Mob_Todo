package com.example.timerappclone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;

public class PomoTimerActivity extends AppCompatActivity {
    private final static long DEFAULT_WORK_DURATION = 1500000;
    private final static long DEFAULT_BREAK_DURATION = 300000;
    private final static int REQUEST_CODE_SETTINGS = 0;
    private final static int COUNTDOWN_INTERVAL = 150;
    private final static int NOTIFICATION_ID = 0;
    private final static String CHANNEL_ID = "togglechannel";
    private CountDownTimer countDownTimer;
    private TextView countdownTimeLabel;
    private ProgressBar countdownProgressBar;
    private Button playPauseButton;
    private ImageButton refreshButton;
    private ImageButton skipButton;
    private ImageView settingsButton;
    private ConstraintLayout timerLayout;
    private Animation blinking;
    private CharSequence startStatusLabel;
    private CharSequence pauseStatusLabel;
    private CharSequence resumeStatusLabel;
    private long setWorkDurationInMillis = DEFAULT_WORK_DURATION;
    private long setBreakDurationInMillis = DEFAULT_BREAK_DURATION;
    private boolean isCountdownRunning;
    private long currentTotalDurationInMillis;
    private long timeLeftInMillis;
    private boolean isWorkMode;
    private long backPressedTime;
    private int colourPrimary;
    private int colourSecondary;
    private int colourText;
    private int colourBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomo_timer);

        currentTotalDurationInMillis = setWorkDurationInMillis;
        timeLeftInMillis = currentTotalDurationInMillis;

        countDownTimer = new PomodoroTimer(setWorkDurationInMillis, COUNTDOWN_INTERVAL);

        isCountdownRunning = false;
        isWorkMode = true;
        createNotificationChannel();

        startStatusLabel = getResources().getText(R.string.start_status_label);
        pauseStatusLabel = getResources().getText(R.string.pause_status_label);
        resumeStatusLabel = getResources().getText(R.string.resume_status_label);

        timerLayout = findViewById(R.id.timerLayout);

        countdownTimeLabel = findViewById(R.id.timerTextView);
        countdownProgressBar = findViewById(R.id.countdownProgressBar);
        playPauseButton = findViewById(R.id.play_pause);
        refreshButton = findViewById(R.id.refreshButton);
        skipButton = findViewById(R.id.skipButton);
        settingsButton = findViewById(R.id.configButton);

        playPauseButton.setOnClickListener(new ButtonListener());
        refreshButton.setOnClickListener(new ButtonListener());
        skipButton.setOnClickListener(new ButtonListener());
        settingsButton.setOnClickListener(new ButtonListener());

        blinking = new AlphaAnimation(0.0f, 1.0f);
        blinking.setDuration(500);
        blinking.setStartOffset(20);
        blinking.setRepeatMode(Animation.REVERSE);
        blinking.setRepeatCount(Animation.INFINITE);

        setProgressBarColour(colourPrimary);
        countdownTimeLabel.startAnimation(blinking);
        updateWidgetColourScheme();
        updateTimerWidgets();
    }

    class PomodoroTimer extends CountDownTimer {
        PomodoroTimer(long countdownInMillis, long countdownInterval) {
            super(countdownInMillis, countdownInterval);
            timeLeftInMillis = countdownInMillis;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeftInMillis = millisUntilFinished;
            updateTimerWidgets();
        }

        @Override
        public void onFinish() {
            toggleWorkMode();
            if (isWorkMode) {
                countdownTimeLabel.setText(R.string.countdown_work_label);
                countdownTimeLabel.setTextColor(colourPrimary);
            } else {
                countdownTimeLabel.setText(R.string.countdown_break_label);
                countdownTimeLabel.setTextColor(colourSecondary);
            }
            // Set countdown progressbar to 0
            countdownProgressBar.setProgress(0);
            timerStandby();
            sendTimerToggleNotification();
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager channelManager = getSystemService(NotificationManager.class);
            try {
                channelManager.createNotificationChannel(channel);
            } catch (NullPointerException exception) {
                Log.d("notificationChannel", "Unable to create notification channel");
            }
        }
    }

    private void sendTimerToggleNotification() {
        String text;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(
                getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), PomoTimerActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isWorkMode) {
            text = "Break is over, time to get to work!";
        } else {
            text = "Session done, time for a break!";
        }

        // Initiate notification with the correct/wanted properties.
        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID)
                .setContentTitle("VNote")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        // Send notification.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private static void cancelNotification(Context context, int notifyId) {

        NotificationManager cancelManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        try {
            cancelManager.cancel(notifyId);
        }
        catch (NullPointerException exception) {
            Log.d("cancelNotification", "Attempted to cancel non-existent notification");
        }
    }


    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {

            cancelNotification(getApplicationContext(), NOTIFICATION_ID);

            if (v.getId() == R.id.configButton) {
                openSettingsActivity();
            }
            else if (v.getId() == R.id.play_pause) {
                if (!isCountdownRunning) {
                    startResumeTimer();
                }
                else {
                    pauseTimer();
                }
            }
            else if (v.getId() == R.id.refreshButton) {
                cancelTimer();
            } else if (v.getId() == R.id.skipButton) {
                skipTimer();
            }
        }
    }

    private void startResumeTimer() {
        countDownTimer = new PomodoroTimer(timeLeftInMillis, COUNTDOWN_INTERVAL);
        countdownTimeLabel.setTextColor(colourText);
        countDownTimer.start();
        timerStartup();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        if (isWorkMode) {
            countdownTimeLabel.setTextColor(colourPrimary);
        }
        else {
            countdownTimeLabel.setTextColor(colourSecondary);
        }
        timerStandby();
    }

    private void cancelTimer() {
        countDownTimer.cancel();
        toggleWorkMode();
        updateTimerWidgets();
        countdownTimeLabel.setTextColor(colourText);
        timerStandby();
    }

    private void skipTimer() {
        countDownTimer.cancel();
        toggleWorkMode();
        updateTimerWidgets();

        countdownTimeLabel.setTextColor(colourText);

        timerStandby();
    }

    private void timerStandby() {
        if (timeLeftInMillis != currentTotalDurationInMillis) {
            playPauseButton.setText(resumeStatusLabel);
        }
        else {
            playPauseButton.setText(startStatusLabel);
        }

        isCountdownRunning = false;
        countdownTimeLabel.startAnimation(blinking);
    }

    private void timerStartup() {
        isCountdownRunning = true;
        countdownTimeLabel.clearAnimation();
        playPauseButton.setText(pauseStatusLabel);
    }

    private void updateColourSchemeColour() {
        colourPrimary = getColor(R.color.lightPrimary);
        colourSecondary = getColor(R.color.lightSecondary);
        colourText = getColor(R.color.lightText);
        colourBackground = getColor(R.color.lightBackground);
    }

    private void updateWidgetColourScheme() {
        timerLayout.setBackgroundColor(colourBackground);
        countdownTimeLabel.setTextColor(colourText);

        if (isWorkMode) {
            if (!isCountdownRunning && timeLeftInMillis != currentTotalDurationInMillis) {
                countdownTimeLabel.setTextColor(colourPrimary);
            }
            setProgressBarColour(colourPrimary);
        }
        else {
            if (!isCountdownRunning && timeLeftInMillis != currentTotalDurationInMillis) {
                countdownTimeLabel.setTextColor(colourSecondary);
            }
            setProgressBarColour(colourSecondary);
        }
    }

    private void updateActivityColourScheme() {
        updateColourSchemeColour();
        updateWidgetColourScheme();

    }

    private void updateCurrentTotalTime() {
        if (timeLeftInMillis == currentTotalDurationInMillis) {
            if (isWorkMode) {
                currentTotalDurationInMillis = setWorkDurationInMillis;
            }
            else {
                currentTotalDurationInMillis = setBreakDurationInMillis;
            }
            timeLeftInMillis = currentTotalDurationInMillis;
            updateTimerWidgets();
        }
    }

    private void setProgressBarColour(int colour) {

        // User filtering to change the colour of the progress bar drawable.
        countdownProgressBar.getProgressDrawable().setColorFilter(
                colour, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void toggleWorkMode(){
        if (isWorkMode) {

            isWorkMode = false;
            currentTotalDurationInMillis = setBreakDurationInMillis;
            setProgressBarColour(colourSecondary);
        }
        else {

            isWorkMode = true;
            currentTotalDurationInMillis = setWorkDurationInMillis;
            setProgressBarColour(colourPrimary);
        }
        countDownTimer = new PomodoroTimer(currentTotalDurationInMillis, COUNTDOWN_INTERVAL);
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), PomoSettingsActivity.class);
        intent.putExtra("setWorkDurationInMillis", setWorkDurationInMillis);
        intent.putExtra("setBreakDurationInMillis", setBreakDurationInMillis);

        // Start activity with request for to reference it again when the settings activity is done.
        startActivityForResult(intent, REQUEST_CODE_SETTINGS);

    }

//    save setting
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode)
        {
            case REQUEST_CODE_SETTINGS:

                if(resultCode == RESULT_OK)
                {
                    setBreakDurationInMillis = dataIntent.getLongExtra("newBreakDurationInMillis",
                            DEFAULT_BREAK_DURATION);
                    setWorkDurationInMillis = dataIntent.getLongExtra("newWorkDurationInMillis",
                            DEFAULT_WORK_DURATION);
                    updateActivityColourScheme();
                    updateCurrentTotalTime();
                }
        }
    }

    private void updateTimerWidgets() {
        updateCountDownText();
        int progressPercent = (int)(100.0 * timeLeftInMillis / currentTotalDurationInMillis);

        if (isCountdownRunning && progressPercent == 0 && timeLeftInMillis > 1000) {
            countdownProgressBar.setProgress(1);
        }
        else {
            countdownProgressBar.setProgress(progressPercent);
        }
    }

    private void updateCountDownText() {
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int)(timeLeftInMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d",
                minutes, seconds);
        countdownTimeLabel.setText(timeLeft);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());

        }
        else {
            Toast.makeText(getApplicationContext(), "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
            backPressedTime = System.currentTimeMillis();
        }
    }

}

