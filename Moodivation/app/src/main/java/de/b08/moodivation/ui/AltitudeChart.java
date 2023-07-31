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

import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.b08.moodivation.R;
import de.b08.moodivation.database.location.LocationDatabase;
import de.b08.moodivation.database.location.entities.LocationHistoryEntity;
import de.b08.moodivation.utils.DateUtils;

public class AltitudeChart extends IntervalDateLineChart {
    public AltitudeChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitle(R.string.altitudeChartTitle);
        getChart().getAxisLeft().setAxisMinimum(0);
        setResetMin(false);
        syncChart();
    }

    @Override
    public void syncChart() {
        AsyncTask.execute(() -> {
            List<LocationHistoryEntity> locations = LocationDatabase.getInstance(getContext()).locationHistoryDao()
                    .getLocationsInTimeInterval(DateUtils.minimizeDay(from).getTime(), DateUtils.maximizeDay(to).getTime());
            Date minDate = locations.stream().map(l -> l.timestamp).min(Date::compareTo).orElse(new Date());

            List<Entry> entries = locations.stream()
                    .map(l -> new Entry((float) l.timestamp.getTime() - minDate.getTime(), (float) l.altitude))
                    .collect(Collectors.toList());

            setData(entries, minDate.getTime());
        });
    }

}
