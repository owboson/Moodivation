package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.dao.InterventionRecordDao;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.database.questionnaire.entities.QuestionnaireNotesEntity;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;


public class RecordsPage extends AppCompatActivity {

    InterventionDatabase db;
    InterventionRecordDao recordDao;
    InterventionLoader interventionLoader;

    private RecyclerView recyclerView;
//    private RecordAdapter recordAdapter;
    private List<InterventionRecordEntity> recordList;

    LayoutInflater inflater;
    LinearLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_page);

        inflater = LayoutInflater.from(getApplicationContext());
        parentView = findViewById(R.id.parentLayout); // Assuming you have a LinearLayout as the parent layout

        InterventionDatabase interventionDatabase = InterventionDatabase.getInstance(getApplicationContext());
        interventionLoader = new InterventionLoader();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<InterventionRecordEntity> records = interventionDatabase.interventionRecordDao().getAllRecords();
                for (final InterventionRecordEntity record : records) {
                    final Optional<InterventionBundle> interventionBundle = interventionLoader.getInterventionWithId(record.interventionId, getApplicationContext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = inflater.inflate(R.layout.loop_item, parentView, false);
                            String title_val = "";
                            TextView textViewTitle = view.findViewById(R.id.record_title);
                            TextView from = view.findViewById(R.id.from);
                            TextView to = view.findViewById(R.id.to);

                            if (interventionBundle.isPresent()) {
                                InterventionBundle bundle = interventionBundle.get();
                                // Access the intervention or intervention map from the bundle
                                Intervention intervention = bundle.getInterventionMap().values().stream().findFirst().orElse(null);
                                if (intervention != null) {
                                    String title = intervention.getTitle();
                                    title_val = title;
//                                    textViewTitle.setText(title);
                                }
                            }

                            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss", Locale.US);

                            try {
                                String fromLabelText = "From: ";
                                String toLabelText = "To: ";

                                Date date = inputFormat.parse(String.valueOf(record.startTimestamp));
                                Date date2 = inputFormat.parse(String.valueOf(record.endTimestamp));

                                long durationInMillis = date2.getTime() - date.getTime();
                                long durationInHours = durationInMillis / (60 * 60 * 1000);
                                long durationInMinutes = durationInMillis / (60 * 1000);
                                long durationInSeconds = durationInMillis / 1000;
                                title_val += " (" + durationInHours + " hrs. " + durationInMinutes+ " min. " + durationInSeconds+" sec.) ";
                                textViewTitle.setText(title_val);

                                String startDate = outputFormat.format(date);
                                String endDate = outputFormat.format(date2);

                                fromLabelText = fromLabelText+ startDate;
                                toLabelText += endDate;

                                from.setText(fromLabelText);
                                to.setText(toLabelText);



                                parentView.addView(view);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });

//        AsyncTask.execute(() -> {
//            List<InterventionRecordEntity> records = interventionDatabase.interventionRecordDao().getAllRecords();
//            for( InterventionRecordEntity record : records) {
//                Optional<InterventionBundle> interventionBundle = interventionLoader.getInterventionWithId(record.interventionId, getApplicationContext());
//                System.out.println(interventionBundle);
//                View view = inflater.inflate(R.layout.loop_item, parentView, false);
//                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
//                TextView textViewDescription = view.findViewById(R.id.textViewDescription);
//
//                if (interventionBundle.isPresent()) {
//                    InterventionBundle bundle = interventionBundle.get();
//                    // Access the intervention or intervention map from the bundle
//                    Intervention intervention = bundle.getInterventionMap().values().stream().findFirst().orElse(null);
//
//                    // Use the intervention as needed
//                    System.out.println(intervention.getDescription());
//
//                    if (intervention != null) {
//                        // Access the intervention's properties (title, description, media, etc.)
//                        String title = intervention.getTitle();
//                        String description = intervention.getDescription();
//                        System.out.println(title);
//
//                        textViewDescription.setText(intervention.getTitle());
//
//                    }
//                }
//                interventionLoader.getInterventionWithId(record.interventionId, getApplicationContext());
//                System.out.println(record.interventionId);
//                System.out.println(record.questionnaireAnswerId);
//                System.out.println(record.startTimestamp);
//                System.out.println(record.endTimestamp);
//                System.out.println(record.afterQuestionnaire);
//
//                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//                SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss", Locale.US);
//                try {
//                    Date date = inputFormat.parse(String.valueOf(record.startTimestamp));
//                    String formattedDate = outputFormat.format(date);
//                    System.out.println(formattedDate);
//
//
//                    // Set data for each view
//                    textViewTitle.setText(formattedDate);
//
//                    // Add the view to the parent layout
//                    parentView.addView(view);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
    }
}