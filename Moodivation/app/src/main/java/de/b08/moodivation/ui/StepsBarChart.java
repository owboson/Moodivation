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
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.b08.moodivation.R;
import de.b08.moodivation.database.sensors.SensorDatabase;
import de.b08.moodivation.utils.DateUtils;

public class StepsBarChart extends IntervalDateBarChart {

    public StepsBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitle(R.string.stepsChartTitle);
        getChart().getAxisLeft().setAxisMinimum(0);
        setResetMin(false);
        syncChart();
    }

    @Override
    public void syncChart() {
        AsyncTask.execute(() -> {
            List<BarEntry> entries = new ArrayList<>();

            Date from = DateUtils.minimizeDay(this.from);
            Date to = DateUtils.maximizeDay(this.to);
            int days = (int) (to.getTime() - from.getTime())/(1000*60*60*24);
            List<Date> dates = new ArrayList<>();

            Date current = from;
            for (int i = 0; i < days; i++) {
                int val = SensorDatabase.getInstance(getContext()).stepDataDao().getStepsInTimeInterval(current.getTime(), current.getTime() + 1000*60*60*24 - 1);
                entries.add(new BarEntry(i+1, val));
                dates.add(new Date(current.getTime()));
                current = new Date(current.getTime() + 1000*60*60*24);
            }

            setIndexDateMapper(i -> {
                if (i <= 0 || i > dates.size())
                    return null;

                return dates.get(i-1);
            });

            setData(entries);
        });
    }

}
