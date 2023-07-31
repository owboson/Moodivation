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

package de.b08.moodivation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;

/**
 * List of all stored intervention records
 */
public class RecordsPage extends Fragment {

    InterventionDatabase interventionDatabase;

    LayoutInflater inflater;
    LinearLayout parentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_records_page, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        parentView = view.findViewById(R.id.parentLayout); // Assuming you have a LinearLayout as the parent layout

        interventionDatabase = InterventionDatabase.getInstance(getContext());

        loadRecords();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecords();
    }

    private void addNoRecordsLabel() {
        TextView textView = new TextView(getContext());
        textView.setText(R.string.noRecordsAvailable);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,20,0,0);
        textView.setLayoutParams(layoutParams);

        textView.setTextSize(18);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        parentView.addView(textView);
    }

    private void loadRecords() {
        parentView.removeAllViews();
        Handler mainHandler = new Handler(Looper.getMainLooper());
        AsyncTask.execute(() -> {
            final List<InterventionRecordEntity> records = interventionDatabase.interventionRecordDao().getAllRecords();
            Collections.reverse(records);

            mainHandler.post(() -> {
                if (records.isEmpty())
                    addNoRecordsLabel();
            });

            for (final InterventionRecordEntity record : records) {
                final Optional<InterventionBundle> interventionBundle = InterventionLoader.getInterventionWithId(record.interventionId, getContext());

                mainHandler.post(() -> {
                    View view = inflater.inflate(R.layout.loop_item, parentView, false);
                    String title_val = "";
                    TextView textViewTitle = view.findViewById(R.id.record_title);
                    TextView from = view.findViewById(R.id.from);
                    TextView to = view.findViewById(R.id.to);

                    if (interventionBundle.isPresent()) {
                        InterventionBundle bundle = interventionBundle.get();
                        // Access the intervention or intervention map from the bundle
                        Intervention intervention = InterventionLoader.getLocalizedIntervention(bundle);
                        if (intervention != null) {
                            title_val = intervention.getTitle();
                        }
                    }

                    SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss", Locale.US);

                    try {
                        String fromLabelText = getString(R.string.from) + ": ";
                        String toLabelText = getString(R.string.to) + ": ";

                        Date date = inputFormat.parse(String.valueOf(record.startTimestamp));
                        Date date2 = inputFormat.parse(String.valueOf(record.endTimestamp));

                        long durationInMillis = date2 == null || date == null ? -1 : date2.getTime() - date.getTime();
                        long durationInHours = durationInMillis / (60 * 60 * 1000);
                        long durationInMinutes = durationInMillis / (60 * 1000);
                        long durationInSeconds = durationInMillis / 1000;
                        title_val += " (" + durationInHours + " hrs. " + durationInMinutes+ " min. " + durationInSeconds+" sec.) ";
                        textViewTitle.setText(title_val);

                        String startDate = date == null ? "" : outputFormat.format(date);
                        String endDate = date2 == null ? "" : outputFormat.format(date2);

                        fromLabelText = fromLabelText+ startDate;
                        toLabelText += endDate;

                        from.setText(fromLabelText);
                        to.setText(toLabelText);
                        final String[] data = { title_val, fromLabelText, toLabelText };

                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), RecordPage.class);
                            intent.putExtra("values", data);
                            intent.putExtra("record", record);
                            startActivity(intent);
                        });

                        parentView.addView(view);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                });
            }
        });
    }
}