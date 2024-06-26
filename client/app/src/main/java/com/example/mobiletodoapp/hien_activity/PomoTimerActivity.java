package com.example.mobiletodoapp.hien_activity;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.showToast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.TimerApi;
import com.example.mobiletodoapp.phuc_activity.api.UserApi;
import com.example.mobiletodoapp.phuc_activity.dto.Login;
import com.example.mobiletodoapp.phuc_activity.dto.User;
import com.example.mobiletodoapp.phuc_activity.view.Login.LoginActivity;
import com.example.mobiletodoapp.thuyen_services.PomodoroActivity;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PomoTimerActivity extends AppCompatActivity {
    private final static long DEFAULT_FOCUS_DURATION = 1500000;
    private final static long DEFAULT_SHORT_BREAK_DURATION = 300000;
    private final static long DEFAULT_LONG_BREAK_DURATION = 900000;
    private final static int DEFAULT_SESSION = 4;
    private final static int REQUEST_CODE_SETTINGS = 0;
    private final static int COUNTDOWN_INTERVAL = 150;
    private final static int NOTIFICATION_ID = 0;
    private final static String CHANNEL_ID = "togglechannel";
    private CountDownTimer countDownTimer;
    private TextView countdownTimeLabel;
    private ProgressBar countdownProgressBar;
    private long timerStartTime;
    public long duringTime; 
    private ImageButton playPauseButton, pauseButton;
    private ImageButton refreshButton, skipButton;
    private ImageView settingsButton, btnBackToPomodoro;
    ;
    private ConstraintLayout timerLayout;
    private Animation blinking;
    private long setFocusDurationInMillis = DEFAULT_FOCUS_DURATION;
    private long setShortBreakDurationInMillis = DEFAULT_SHORT_BREAK_DURATION;
    private long setLongBreakDurationInMillis = DEFAULT_LONG_BREAK_DURATION;
    private int sessionCount = 0;
    private int sessionsBeforeLongBreak = DEFAULT_SESSION;

    private boolean isCountdownRunning;
    private long currentTotalDurationInMillis;
    private long timeLeftInMillis;
    private boolean isFocusMode;
    private int completedFocusSessions = 0;

    private long backPressedTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomo_timer);

        currentTotalDurationInMillis = setFocusDurationInMillis;
        timeLeftInMillis = currentTotalDurationInMillis;

        countDownTimer = new PomodoroTimer(setFocusDurationInMillis, COUNTDOWN_INTERVAL);

        isCountdownRunning = false;
        isFocusMode = true;
        createNotificationChannel();

        duringTime = loadDuringTime();
        timerLayout = findViewById(R.id.timerLayout);

        countdownTimeLabel = findViewById(R.id.timerTextView);
        countdownProgressBar = findViewById(R.id.countdownProgressBar);
        playPauseButton = findViewById(R.id.play_pause);
        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.INVISIBLE);
        refreshButton = findViewById(R.id.refreshButton);
        skipButton = findViewById(R.id.skipButton);
        settingsButton = findViewById(R.id.configButton);

        playPauseButton.setOnClickListener(new ButtonListener());
        refreshButton.setOnClickListener(new ButtonListener());
        skipButton.setOnClickListener(new ButtonListener());
        settingsButton.setOnClickListener(new ButtonListener());
        pauseButton.setOnClickListener(new ButtonListener());

        blinking = new AlphaAnimation(0.0f, 1.0f);
        blinking.setDuration(500);
        blinking.setStartOffset(20);
        blinking.setRepeatMode(Animation.REVERSE);
        blinking.setRepeatCount(Animation.INFINITE);

        countdownTimeLabel.startAnimation(blinking);
        updateTimerWidgets();

        btnBackToPomodoro = findViewById(R.id.btn_back_to_previous);
        btnBackToPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToPomodoroActivity();
            }
        });
    }

    private void returnToPomodoroActivity() {
        Intent intent = new Intent(this, PomodoroActivity.class);
        startActivity(intent);
        finish();
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
            toggleFocusMode();
            if (isFocusMode) {
                countdownTimeLabel.setText(R.string.countdown_focus_label);
            } else {
                countdownTimeLabel.setText(R.string.countdown_break_label);
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

        if (isFocusMode) {
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
            else if (v.getId() == R.id.pauseButton) {
                pauseTimer();
            }

        }
    }

    private void startResumeTimer() {
        timerStartTime = System.currentTimeMillis();
        countDownTimer = new PomodoroTimer(timeLeftInMillis, COUNTDOWN_INTERVAL);
        countDownTimer.start();
        timerStartup();
        playPauseButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        long duration = System.currentTimeMillis() - timerStartTime;
        duringTime += duration;
        saveDuringTime(duringTime);
        countDownTimer.cancel();
        timerStandby();
        playPauseButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
    }

    private void cancelTimer() {
        countDownTimer.cancel();
        toggleFocusMode();
        updateTimerWidgets();
        timerStandby();
//        sendTime();
    }
//    private CompletableFuture<Void> sendTime() {
//        CompletableFuture<Void> future = new CompletableFuture<>();
//        Login login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString());
//        userApi.loginUser(login).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                try {
//                    processLoginResponse(response);
//                    future.complete(null);
//                } catch (Exception e) {
//                    future.completeExceptionally(e);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                hideLoading();
//                showToast(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại đường truyền của bạn.");
//                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error: ", t);
//                future.completeExceptionally(t);
//            }
//        });
//
//        return future;
//    }

    private void skipTimer() {
        countDownTimer.cancel();
        toggleFocusMode();
        updateTimerWidgets();

        timerStandby();
    }

    private void timerStandby() {
        if (timeLeftInMillis != currentTotalDurationInMillis) {
        } else {
        }
        isCountdownRunning = false;
        countdownTimeLabel.startAnimation(blinking);
    }

    private void timerStartup() {
        isCountdownRunning = true;
        countdownTimeLabel.clearAnimation();
    }

    private void updateCurrentTotalTime() {
        if (timeLeftInMillis == currentTotalDurationInMillis) {
            if (isFocusMode) {
                currentTotalDurationInMillis = setFocusDurationInMillis;
            }
            else {
                currentTotalDurationInMillis = setShortBreakDurationInMillis;
            }
            timeLeftInMillis = currentTotalDurationInMillis;
            updateTimerWidgets();
        }
    }

    private void resetDuringTime() {
        duringTime = 0;
        saveDuringTime(duringTime);
    }

    private long loadDuringTime() {
        SharedPreferences prefs = getSharedPreferences("TimerPrefs", MODE_PRIVATE);
        return prefs.getLong("duringTime", 0);
    }

    private void saveDuringTime(long duration) {
        SharedPreferences prefs = getSharedPreferences("TimerPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("duringTime", duration);
        editor.apply();
    }
    private void setProgressBarColour(int colour) {

        // User filtering to change the colour of the progress bar drawable.
        countdownProgressBar.getProgressDrawable().setColorFilter(
                colour, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void toggleFocusMode(){
        if (isFocusMode) {
            completedFocusSessions++;
            // Check if it's time for a long break
            if (completedFocusSessions >= sessionsBeforeLongBreak) {
                isFocusMode = false;
                currentTotalDurationInMillis = setLongBreakDurationInMillis;
                completedFocusSessions = 0; // Reset the counter after a long break
            } else {
                isFocusMode = false;
                currentTotalDurationInMillis = setShortBreakDurationInMillis;
            }
        }
        else {
            isFocusMode = true;
            currentTotalDurationInMillis = setFocusDurationInMillis;
        }
        countDownTimer = new PomodoroTimer(currentTotalDurationInMillis, COUNTDOWN_INTERVAL);
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), PomoSettingsActivity.class);
        intent.putExtra("setFocusDurationInMillis", setFocusDurationInMillis);
        intent.putExtra("setBreakDurationInMillis", setShortBreakDurationInMillis);
        intent.putExtra("setLongBreakDurationInMillis", setLongBreakDurationInMillis);
        intent.putExtra("sessionsBeforeLongBreak", sessionsBeforeLongBreak);
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
                    setShortBreakDurationInMillis = dataIntent.getLongExtra("newShortBreakDurationInMillis",
                            DEFAULT_SHORT_BREAK_DURATION);
                    setFocusDurationInMillis = dataIntent.getLongExtra("newFocusDurationInMillis",
                            DEFAULT_FOCUS_DURATION);
                    setLongBreakDurationInMillis = dataIntent.getLongExtra("newLongBreakDurationInMillis",
                            DEFAULT_LONG_BREAK_DURATION);
                    sessionsBeforeLongBreak = dataIntent.getIntExtra("newSessionsBeforeLongBreak",
                            DEFAULT_SESSION);
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

