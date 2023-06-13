package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;
import java.util.Date;
import java.util.List;

class RecordEntry {
    public String activityTitle;
    public String date;

    public RecordEntry() {
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

class BarEntryValue {
    public List<String> activityTitle;
    public String date;

    public BarEntryValue() {
        this.activityTitle = new ArrayList<>();
    }

    public void addTitle(String activityTitle) {
        this.activityTitle.add(activityTitle);
    }

    public void setDate(String date) {
        this.date = date;
    }
}

public class DevelopmentVisualization extends AppCompatActivity {
    private BarChart barChart;
    InterventionLoader interventionLoader;
    int maxNumber = 0;
    List<BarEntryValue> barEntryValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development_visualization);
        InterventionDatabase interventionDatabase = InterventionDatabase.getInstance(getApplicationContext());
        interventionLoader = new InterventionLoader();

        barChart = findViewById(R.id.barChart);
        List<RecordEntry> chartEntries = new ArrayList<>();
        AsyncTask.execute(() -> {
            List<InterventionRecordEntity> records = interventionDatabase.interventionRecordDao().getAllRecords();
            for (InterventionRecordEntity record : records) {
                RecordEntry entry = new RecordEntry();

                Optional<InterventionBundle> interventionBundle = interventionLoader.getInterventionWithId(record.interventionId, getApplicationContext());

                if (interventionBundle.isPresent()) {
                    InterventionBundle bundle = interventionBundle.get();
                    Intervention intervention = bundle.getInterventionMap().values().stream().findFirst().orElse(null);

                    if (intervention != null) {
                        String title = intervention.getTitle();
                        entry.setActivityTitle(title);
                    }
                }
                interventionLoader.getInterventionWithId(record.interventionId, getApplicationContext());
//                System.out.println(record.interventionId);
//                System.out.println(record.questionnaireAnswerId);
//                System.out.println(record.startTimestamp);
//                System.out.println(record.endTimestamp);
//                System.out.println(record.afterQuestionnaire);

                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                try {
                    Date date = inputFormat.parse(String.valueOf(record.startTimestamp));
                    String formattedDate = outputFormat.format(date);
                    entry.setDate(formattedDate);
                    System.out.println(formattedDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                chartEntries.add(entry);

            }


            for(RecordEntry e : chartEntries) {
                boolean containsDate = false;
                for (BarEntryValue barEntryValue : barEntryValues) {
                    if (barEntryValue.date.equals(e.date)) {
                        containsDate = true;
                        barEntryValue.addTitle(e.activityTitle);
                    }
                }
                if (containsDate == false) {
                    BarEntryValue val = new BarEntryValue();
                    val.addTitle(e.activityTitle);
                    val.date = e.date;
                    barEntryValues.add(val);
                }
            }

            setupBarChart();
            setData();
        });


    }

    private void setupBarChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int totalDays = 7; // Total number of days to display
                int reversedIndex = totalDays - Math.round(value) - 1; // Calculate the reversed index
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -reversedIndex);
                Date date = calendar.getTime();
                return sdf.format(date);
            }
        });

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
    }

    private static int calculateIndexFromDate(Date inputDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        long diffMillis = currentDate.getTimeInMillis() - calendar.getTimeInMillis();
        int diffDays = (int) (diffMillis / (24 * 60 * 60 * 1000));

        return 7 - diffDays;
    }

    private void setData()  {
        List<BarEntry> entries = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        // Replace this with your actual data for the last 7 days
        for (BarEntryValue barEntryValue : barEntryValues) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = sdf.parse(barEntryValue.date);
                int index = calculateIndexFromDate(date);
                System.out.println(index);
                System.out.println(barEntryValue.activityTitle.size());
                entries.add(new BarEntry(index, barEntryValue.activityTitle.size()));
                indices.add(index);

            } catch (ParseException e) {

            }
        }
        for (int i = 0; i < 7; i++) {
            if (indices.contains(i) == false) {
                entries.add(new BarEntry(i, 0));
            }
        }
//
//        entries.add(new BarEntry(5, 8));
//        entries.add(new BarEntry(4, 3));
//        entries.add(new BarEntry(3, 12));
//        entries.add(new BarEntry(2, 7));
//        entries.add(new BarEntry(1, 10));
//        entries.add(new BarEntry(0, 6));

        BarDataSet dataSet = new BarDataSet(entries, "Activity Count");
        dataSet.setColor(Color.BLUE);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.invalidate();
    }
}