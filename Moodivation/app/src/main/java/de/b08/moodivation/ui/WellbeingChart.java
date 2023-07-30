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
import androidx.core.util.Pair;

import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.b08.moodivation.R;
import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.questionnaire.WellbeingAlgorithm;
import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.utils.DateUtils;

public class WellbeingChart extends IntervalDateLineChart {

    public WellbeingChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitle(R.string.wellbeingChartTitle);
        getChart().getAxisLeft().setAxisMinimum(0);
        getChart().getAxisLeft().setAxisMaximum(1);
        setResetMin(false);
        setResetMax(false);
        syncChart();
    }

    @Override
    public void syncChart() {
        Date from = DateUtils.minimizeDay(this.from);
        Date to = DateUtils.maximizeDay(this.to);
        AsyncTask.execute(() -> {
            QuestionnaireDatabase questionnaireDatabase = QuestionnaireDatabase.getInstance(getContext());
            List<Date> dates = questionnaireDatabase.answerDao().getAllDates();
            Date minDate = dates.stream().min(Date::compareTo).orElse(new Date());
            List<Entry> entries = dates.stream()
                    .filter(d -> d.before(to) && d.after(from) || d.equals(to) || d.equals(from))
                    .map(d -> questionnaireDatabase.answerDao().getAllAnswerEntitiesWithTimestamp(d.getTime()))
                    .map(l -> new Pair<>(new Date(l.get(0).timestamp.getTime() - minDate.getTime()), l.stream().map(Answer::from).collect(Collectors.toList())))
                    .map(p -> new Entry((float) p.first.getTime(), (float) WellbeingAlgorithm.INSTANCE.calculateWellbeing(p.second)))
                    .collect(Collectors.toList());

            setData(entries, minDate.getTime());
        });
    }



}
