package de.b08.moodivation.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class IntervalDateLineChart extends DateLineChart {

    Date from;
    Date to;

    final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public IntervalDateLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TextView fromToLabel = new TextView(getContext(), attrs);
        fromToLabel.setTextSize(14);
        fromToLabel.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        fromToLabel.setTextColor(Color.BLACK);

        getAdditionalComponentsPanel().addView(fromToLabel);

        this.from = new Date(new Date().getTime() - 7*24*60*60*1000);
        this.to = new Date();

        fromToLabel.setText(String.format("%s - %s", format.format(this.from), format.format(this.to)));

        fromToLabel.setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setSelection(new Pair<>(this.from.getTime(), this.to.getTime()))
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                this.from = new Date(selection.first);
                this.to = new Date(selection.second);
                fromToLabel.setText(String.format("%s - %s", format.format(from), format.format(to)));
                syncChart();
            });

            datePicker.show(((FragmentActivity) context).getSupportFragmentManager(), "DATE_RANGE");
        });
    }

}
