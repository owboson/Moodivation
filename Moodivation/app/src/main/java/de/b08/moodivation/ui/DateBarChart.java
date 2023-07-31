/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.b08.moodivation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
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
import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.b08.moodivation.R;

public abstract class DateBarChart extends LinearLayout {

    private final BarChart chart;

    private final TextView chartTitle;

    private final LinearLayout additionalComponentsPanel;

    private Function<Integer, Date> indexDateMapper = i -> new Date();

    private boolean resetMin = true;
    private boolean resetMax = true;

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
        chart.setNoDataText(getResources().getText(R.string.noDataAvailable).toString());
        chart.setNoDataTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        chart.setDrawBarShadow(false);
        chart.setOnChartValueSelectedListener(null);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);

        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

        SimpleDateFormat format = new SimpleDateFormat("dd.MM", Locale.getDefault());
        chart.getXAxis().setValueFormatter((value, axis) -> {
            Date d = indexDateMapper.apply((int) value);
            return d == null ? "" : format.format(d);
        });

        chart.getAxisLeft().setValueFormatter((value, axis) -> Float.toString(value));
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setGranularity(1f);
        chart.getAxisLeft().setLabelCount(10);
        chart.getAxisLeft().setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));

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
        dataSet.setBarBorderWidth(1f);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSecondaryContainer));
        // data.setDrawValues(false);

        data.setBarWidth(0.5f);

        chart.getXAxis().setLabelCount(values.size() + 2);

        float minY = values.stream().map(Entry::getY).min(Float::compareTo).orElse(0F);
        float maxY = values.stream().map(Entry::getY).max(Float::compareTo).orElse(0F);
        if (resetMin)
            chart.getAxisLeft().setAxisMinimum(minY);
        if (resetMax)
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

    public BarChart getChart() {
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
