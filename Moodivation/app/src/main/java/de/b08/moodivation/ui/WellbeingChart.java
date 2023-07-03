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

public class WellbeingChart extends IntervalDateLineChart {

    public WellbeingChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitle(R.string.wellbeingChartTitle);
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
