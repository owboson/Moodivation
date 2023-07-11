package de.b08.moodivation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.b08.moodivation.R;

public abstract class DateLineChart extends LinearLayout {

    private LineChart chart;

    private TextView chartTitle;

    private LinearLayout additionalComponentsPanel;

    private boolean resetMin = true;
    private boolean resetMax = true;

    public DateLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.lastweek_day_line_chart, this);

        chart = findViewById(R.id.dayBarChart);
        chartTitle = findViewById(R.id.chartTitle);
        additionalComponentsPanel = findViewById(R.id.additionalComponentsPanel);

        setupChart();
        findViewById(R.id.syncChartBtn).setOnClickListener(v -> syncChart());
    }

    public abstract void syncChart();

    private void setupChart() {
        chart.setNoDataText(getResources().getText(R.string.noDataAvailable).toString());
        chart.setNoDataTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setOnChartValueSelectedListener(null);

        chart.getLegend().setEnabled(false);

        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setCenterAxisLabels(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setDrawLabels(true);
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setValueFormatter((value, axis) -> Float.toString(value));
        chart.getAxisLeft().setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));
        chart.getAxisLeft().setGranularity(0.1f);

        chart.getAxisRight().setEnabled(false);
    }

    public <T> void setData(List<T> values, Function<T,Entry> entryFunction, long minDate) {
        setData(values.stream().map(entryFunction).collect(Collectors.toList()), minDate);
    }

    public void setData(List<Entry> values, long minDate) {
        if (values.isEmpty()) {
            chart.clear();
            chart.invalidate();
            return;
        }

        LineDataSet dataSet = new LineDataSet(values,"");
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleRadius(3);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));
        dataSet.setValueTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        LineData data = new LineData(dataSet);
        data.setValueTextSize(14f);
        data.setDrawValues(false);
        data.setValueTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        float minY = values.stream().map(Entry::getY).min(Float::compareTo).orElse(0F);
        float maxY = values.stream().map(Entry::getY).max(Float::compareTo).orElse(0F);
        if (resetMin)
            chart.getAxisLeft().setAxisMinimum(minY);
        if (resetMax)
            chart.getAxisLeft().setAxisMaximum(maxY);

        float minX = values.stream().map(Entry::getX).min(Float::compareTo).orElse(0F);
        float maxX = values.stream().map(Entry::getX).max(Float::compareTo).orElse(0F);
        chart.getXAxis().setAxisMinimum(minX);
        chart.getXAxis().setAxisMaximum(maxX);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM", Locale.getDefault());
        chart.getXAxis().setValueFormatter((value, axis) -> format.format(new Date((long) value + minDate)));

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

    public LineChart getChart() {
        return chart;
    }

    public void setResetMin(boolean resetMin) {
        this.resetMin = resetMin;
    }

    public void setResetMax(boolean resetMax) {
        this.resetMax = resetMax;
    }

    public void setManualReloadingAllowed(boolean allowed) {
        ImageButton syncButton = findViewById(R.id.syncChartBtn);
        syncButton.setEnabled(allowed);
        syncButton.setVisibility(allowed ? VISIBLE : GONE);
    }
}
