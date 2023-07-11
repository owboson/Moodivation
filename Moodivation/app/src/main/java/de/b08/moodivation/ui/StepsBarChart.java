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

            /*
            Date d1 = new Date();
            Date d2 = new Date(d1.getTime() - 1000*60*60*24);
            Date d3 = new Date(d1.getTime() - 1000*60*60*24*2);
            Date d4 = new Date(d1.getTime() - 1000*60*60*24*3);
            Date d5 = new Date(d1.getTime() - 1000*60*60*24*4);
            Date d6 = new Date(d1.getTime() - 1000*60*60*24*5);
            Date d7 = new Date(d1.getTime() - 1000*60*60*24*6);
            Date d8 = new Date(d1.getTime() - 1000*60*60*24*7);
            Date d9 = new Date(d1.getTime() - 1000*60*60*24*8);

            List<Pair<Date, Integer>> data = Arrays.asList(new Pair<>(d1, 2000), new Pair<>(d2, 3000), new Pair<>(d3, 6000), new Pair<>(d4, 1000),
                    new Pair<>(d5, 2000), new Pair<>(d6, 3000), new Pair<>(d7, 6000), new Pair<>(d8, 1000), new Pair<>(d9, 1000));

            data.sort(Comparator.comparing(o -> o.first));

            for (int i = 0; i < data.size(); i++) {
                entries.add(new BarEntry(i+1, data.get(i).second));
            }

            setIndexDateMapper(i -> {
                if (i == 0 || i == data.size() + 1)
                    return null;

                return data.get(i-1).first;
            });
            */

            setData(entries);
        });
    }

}
