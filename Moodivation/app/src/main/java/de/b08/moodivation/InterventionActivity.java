package de.b08.moodivation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.dao.InterventionRecordEntity;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.view.InterventionView;
import de.b08.moodivation.services.LocationLiveData;
import de.b08.moodivation.services.LocationService;
import de.b08.moodivation.services.StepLiveData;
import de.b08.moodivation.services.TravelledDistanceLiveDataManager;
import de.b08.moodivation.ui.DescribedChronometerView;
import de.b08.moodivation.ui.DescribedValueView;

/**
 * Activity for an ongoing intervention.
 */
public class InterventionActivity extends AppCompatActivity {

    public static final String INTERVENTION_EXTRA_KEY = "intervention";

    private Intervention intervention;

    private InterventionView interventionView;
    private DescribedChronometerView stopWatchValueView;

    private LinearLayout dataView;

    private Button startButton;
    private Button stopButton;

    private boolean isRunning = false;

    private Date startTime;
    private Date endTime;

    private TravelledDistanceLiveDataManager.TravelledDistanceLiveData travelledDistanceLiveData;

    private DescribedValueView stepsView;
    private DescribedValueView distanceView;
    private DescribedValueView speedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ongoing_intervention_activity);
        intervention = (Intervention) getIntent().getExtras().get(INTERVENTION_EXTRA_KEY);

        if (intervention == null) {
            throw new RuntimeException("no intervention set");
        }

        interventionView = findViewById(R.id.interventionView);
        dataView = findViewById(R.id.dataView);

        startButton = findViewById(R.id.startButton);
        startButton.setText(R.string.startButtonText);

        stopButton = findViewById(R.id.stopButton);
        stopButton.setText(R.string.stopButtonText);

        interventionView.setIntervention(intervention);

        if (intervention.getCollectedDataTypes() != null) {
            for (Intervention.DataType collectedDataType : intervention.getCollectedDataTypes()) {
                addDataTypeView(collectedDataType);
            }
        }

        startButton.setOnClickListener(v -> start());

        stopButton.setOnClickListener(v -> stop());

        reset();
    }

    public void start() {
        reset();
        isRunning = true;
        startTime = new Date();
        if (stopWatchValueView != null)
            stopWatchValueView.start();
    }

    public void resume() {
        // TODO: implement
    }

    private void reset() {
        startTime = null;
        endTime = null;
        isRunning = false;
        if (travelledDistanceLiveData != null) {
            travelledDistanceLiveData.reset();
        }
        if (speedView != null) {
            speedView.setValueText("-");
        }
        if (distanceView != null) {
            distanceView.setValueText("-");
        }
        if (stepsView != null) {
            stepsView.setValueText("-");
        }
    }

    public void stop() {
        isRunning = false;
        endTime = new Date();

        if (stopWatchValueView != null)
            stopWatchValueView.stop();

        AsyncTask.execute(() -> {
            boolean afterQuestionnaire = getIntent().getBooleanExtra("afterQuestionnaire", false);
            long questionnaireAnswerId = getIntent().getLongExtra("questionnaireAnswerId", -1);

            InterventionRecordEntity interventionRecord = new InterventionRecordEntity(intervention.getId(),
                    questionnaireAnswerId == -1 ? null : new Date(questionnaireAnswerId), startTime, endTime, afterQuestionnaire);

            InterventionDatabase.getInstance(getApplicationContext())
                    .interventionRecordDao().insert(interventionRecord);

            finish();
        });
    }

    private void addDataTypeView(Intervention.DataType dataType) {
        if (dataType == Intervention.DataType.ELAPSED_TIME) {
            stopWatchValueView = new DescribedChronometerView(getApplicationContext(), null);
            dataView.addView(stopWatchValueView);
            setMargin(stopWatchValueView);
        } else if (dataType == Intervention.DataType.SPEED) {
            speedView = new DescribedValueView(getApplicationContext(), null);
            dataView.addView(speedView);

            LocationLiveData.getInstance().observe(this, location -> {
                if (location.getAccuracy() > LocationService.Constants.TRAVELLED_DISTANCE_MAX_ACCURACY || !isRunning)
                    return;

                speedView.setValueText(Float.toString(location.getSpeed()));
            });

            speedView.setDescriptionText(R.string.speedDescriptionText);
            setMargin(speedView);
        } else if (dataType == Intervention.DataType.TRAVELLED_DISTANCE) {
            distanceView = new DescribedValueView(getApplicationContext(), null);
            dataView.addView(distanceView);

            travelledDistanceLiveData = TravelledDistanceLiveDataManager.getInstance().create();

            travelledDistanceLiveData.observe(this, aDouble -> {
                if (!isRunning)
                    return;

                distanceView.setValueText(Double.toString(aDouble));
            });

            distanceView.setDescriptionText(R.string.travelledDistanceDescriptionText);
            setMargin(distanceView);
        } else if (dataType == Intervention.DataType.STEPS) {
            stepsView = new DescribedValueView(getApplicationContext(), null);
            dataView.addView(stepsView);

            StepLiveData.getInstance().observe(this, integer -> {
                if (!isRunning)
                    return;

                stepsView.setValueText(Integer.toString(integer));
            });
            stepsView.setDescriptionText(R.string.stepDescriptionText);
            setMargin(stepsView);
        }
    }

    private void setMargin(View v) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        marginLayoutParams.setMargins(10, 10,10,10);
    }

}

