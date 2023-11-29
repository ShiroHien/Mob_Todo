package com.example.mobiletodoapp.thuyen_services;

import static com.example.mobiletodoapp.phuc_activity.reusecode.Function.getSharedPref;
import com.example.mobiletodoapp.phuc_activity.dto.Timer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mobiletodoapp.R;
import com.example.mobiletodoapp.phuc_activity.api.RetrofitService;
import com.example.mobiletodoapp.phuc_activity.api.TimerApi;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PomodoroActivity extends AppCompatActivity {

    ImageView btnBackToPrevious;
    Button btnMoveToPomodoro;
    BarChart barChart;
    TimerApi timerApi;

    List<Timer> timers;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        init();
        showLoading();
        getTimersFromServer();



    }

    private void init() {
        btnBackToPrevious = findViewById(R.id.btn_back_to_previous);
        btnMoveToPomodoro = findViewById(R.id.btn_move_to_pomodoro);
        barChart = findViewById(R.id.barchart);

        timerApi = new RetrofitService().getRetrofit().create(TimerApi.class);
    }

    private void setDataForCharBAr() {
        int count = 0;
        List<BarEntry> entries = new ArrayList<>();
        for(int i = 1; i <= timers.size(); i++) {
            entries.add(new BarEntry(i, timers.get(i-1).getDuringTime()));
        }
        // Thay đổi dữ liệu mẫu để phản ánh thời gian làm việc trong 7 ngày gần đây

//        entries.add(new BarEntry(1f, 0.5f)); // Ngày 1
//        entries.add(new BarEntry(2f, 1f)); // Ngày 2
//        entries.add(new BarEntry(3f, 3f)); // Ngày 3
//        entries.add(new BarEntry(4f, 3f)); // Ngày 4
//        entries.add(new BarEntry(5f, 3.5f)); // Ngày 5
//        entries.add(new BarEntry(6f, 0)); // Ngày 6
//        entries.add(new BarEntry(7f, 4f)); // Ngày 7

        BarDataSet dataSet = new BarDataSet(entries, "Thời gian làm việc (giờ)");

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Cấu hình Axis X để hiển thị nhãn ngày
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        // Hiển thị biểu đồ
        barChart.invalidate();
    }

    CompletableFuture<Void> getTimersFromServer() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String userId = getSharedPref(this, "userId", "default id");
//        Log.d("timer", userId);

        timerApi.getMyDayTask(userId).enqueue(new Callback<List<Timer>>() {
            @Override
            public void onResponse(Call<List<Timer>> call, Response<List<Timer>> response) {
                if(response.body() != null) {
                    timers = response.body();
                    setDataForCharBAr();
                    hideLoading();
                    Log.d("get timers", "true");
                } else {
                    Log.d("get timers", "false");
                }
            }

            @Override
            public void onFailure(Call<List<Timer>> call, Throwable t) {
                Log.d("get timers", t.toString());
            }
        });

        return future;
    }



    private List<String> getXAxisLabels() {
        List<String> labels = new ArrayList<>();
        labels.add("Ngày 0");
        labels.add("Ngày 1");
        labels.add("Ngày 2");
        labels.add("Ngày 3");
        labels.add("Ngày 4");
        labels.add("Ngày 5");
        labels.add("Ngày 6");
        labels.add("Ngày 7");
        return labels;
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}