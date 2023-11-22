package com.example.mobiletodoapp.thuyen_services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobiletodoapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class PomodoroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        BarChart barChart = findViewById(R.id.barchart);

        // Thay đổi dữ liệu mẫu để phản ánh thời gian làm việc trong 7 ngày gần đây
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 0.5f)); // Ngày 1
        entries.add(new BarEntry(2f, 1f)); // Ngày 2
        entries.add(new BarEntry(3f, 3f)); // Ngày 3
        entries.add(new BarEntry(4f, 3f)); // Ngày 4
        entries.add(new BarEntry(5f, 3.5f)); // Ngày 5
        entries.add(new BarEntry(6f, 0)); // Ngày 6
        entries.add(new BarEntry(7f, 4f)); // Ngày 7

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
}