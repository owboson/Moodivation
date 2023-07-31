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

    TextView fromToLabel;

    final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public IntervalDateLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        fromToLabel = new TextView(getContext(), attrs);
        fromToLabel.setTextSize(14);
        fromToLabel.setTextAlignment(TEXT_ALIGNMENT_CENTER);

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

    public void setIntervalChangeAllowed(boolean allowed) {
        fromToLabel.setEnabled(allowed);
        fromToLabel.setVisibility(allowed ? VISIBLE : GONE);
    }

}
