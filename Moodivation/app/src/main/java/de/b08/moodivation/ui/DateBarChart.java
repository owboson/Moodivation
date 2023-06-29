package de.b08.moodivation.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.b08.moodivation.R;

public abstract class DateBarChart extends LinearLayout {

    private BarChart chart;

    private TextView chartTitle;

    private LinearLayout additionalComponentsPanel;

    private Function<Integer, Date> indexDateMapper = i -> new Date();

    public DateBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.lastweek_day_bar_chart, this);

        chart = findViewById(R.id.dayBarChart);
        chartTitle = findViewById(R.id.chartTitle);
        additionalComponentsPanel = findViewById(R.id.additionalComponentsPanel);

        setupChart();
        findViewById(R.id.syncChartBtn).setOnClickListener(v -> syncChart());
    }

    public abstract void syncChart();

    private void setupChart() {
        chart.setDrawBarShadow(false);
        chart.setOnChartValueSelectedListener(null);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);

        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM", Locale.getDefault());
        chart.getXAxis().setValueFormatter((value, axis) -> {
            Date d = indexDateMapper.apply((int) value);
            return d == null ? "" : format.format(d);
        });

        chart.getAxisLeft().setValueFormatter((value, axis) -> Float.toString(value));
        chart.getAxisLeft().setTextColor(Color.BLACK);
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setGranularity(1f);
        chart.getAxisLeft().setLabelCount(10);

        chart.getAxisRight().setEnabled(false);

        chart.getLegend().setEnabled(false);
    }

    public <T> void setData(List<T> values, Function<T,BarEntry> entryFunction) {
        setData(values.stream().map(entryFunction).collect(Collectors.toList()));
    }

    public void setData(List<BarEntry> values) {
        if (values.isEmpty()) {
            chart.clear();
            chart.invalidate();
            return;
        }

        BarDataSet dataSet = new BarDataSet(values,"");
        dataSet.setColor(Color.BLACK);
        dataSet.setBarBorderWidth(1f);

        BarData data = new BarData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(14f);
        // data.setDrawValues(false);

        data.setBarWidth(0.5f);

        chart.getXAxis().setLabelCount(values.size() + 2);

        float minY = values.stream().map(Entry::getY).min(Float::compareTo).orElse(0F);
        float maxY = values.stream().map(Entry::getY).max(Float::compareTo).orElse(0F);
        chart.getAxisLeft().setAxisMinimum(minY < 0 ? minY : 0);
        chart.getAxisLeft().setAxisMaximum(maxY);

        float minX = values.stream().map(Entry::getX).min(Float::compareTo).orElse(0F);
        float maxX = values.stream().map(Entry::getX).max(Float::compareTo).orElse(0F);
        chart.getXAxis().setAxisMinimum(minX - 1);
        chart.getXAxis().setAxisMaximum(maxX + 1);

        chart.setData(data);
        chart.invalidate();
    }

    public void setTitle(@StringRes int id) {
        chartTitle.setText(id);
    }

    public void setTitle(String title) {
        chartTitle.setText(title);
    }

    public LinearLayout getAdditionalComponentsPanel() {
        return additionalComponentsPanel;
    }

    public void setIndexDateMapper(Function<Integer, Date> indexDateMapper) {
        this.indexDateMapper = indexDateMapper;
    }
}
