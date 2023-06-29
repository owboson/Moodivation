package de.b08.moodivation.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.b08.moodivation.database.location.LocationDatabase;
import de.b08.moodivation.database.location.entities.LocationHistoryEntity;

public class AltitudeChart extends IntervalDateLineChart {
    public AltitudeChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitle("Elevation Plot");
        syncChart();
    }

    @Override
    public void syncChart() {
        AsyncTask.execute(() -> {
            List<LocationHistoryEntity> locations = LocationDatabase.getInstance(getContext()).locationHistoryDao()
                    .getLocationsInTimeInterval(DateUtils.minimizeDate(from).getTime(), DateUtils.maximizeDate(to).getTime());
            Date minDate = locations.stream().map(l -> l.timestamp).min(Date::compareTo).orElse(new Date());

            List<Entry> entries = locations.stream()
                    .map(l -> new Entry((float) l.timestamp.getTime() - minDate.getTime(), (float) l.altitude))
                    .collect(Collectors.toList());

            setData(entries, minDate.getTime());
        });
    }

}
