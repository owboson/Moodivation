package de.b08.moodivation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.view.InterventionFeedbackView;
import de.b08.moodivation.intervention.view.InterventionView;
import de.b08.moodivation.services.LocationLiveData;
import de.b08.moodivation.services.LocationService;
import de.b08.moodivation.services.StepLiveData;
import de.b08.moodivation.services.TravelledDistanceLiveDataManager;
import de.b08.moodivation.utils.DateUtils;
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

        boolean afterQuestionnaire = getIntent().getBooleanExtra("afterQuestionnaire", false);
        long questionnaireAnswerId = getIntent().getLongExtra("questionnaireAnswerId", -1);

        InterventionRecordEntity interventionRecord = new InterventionRecordEntity(intervention.getId(),
                questionnaireAnswerId == -1 ? null : new Date(questionnaireAnswerId), startTime, endTime,
                afterQuestionnaire, null, null);
        AsyncTask.execute(() -> {
            InterventionDatabase.getInstance(getApplicationContext())
                    .interventionRecordDao().insert(interventionRecord);

            checkRewards();
        });

        showFeedbackAlertAndFinish(interventionRecord.cloneEntity());
    }

    private void checkRewards() {
        if (Rewards.streakCompleted(getApplicationContext()) && !InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                .streakReferenceDateExists(DateUtils.minimizeDay(new Date()).getTime())) {
            InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                    .insert(RewardCompletionEntity.createStreakEntity(DateUtils.minimizeDay(new Date()), new Date()));
        }

        if (intervention.getId().equals("CyclingIntervention_Internal") && Rewards.cyclingCompleted(getApplicationContext())
                && !InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                .cyclingReferenceDateExists(DateUtils.minimizeMonthAndDay(new Date()).getTime())) {
            InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                    .insert(RewardCompletionEntity.createCyclingEntity(DateUtils.minimizeMonthAndDay(new Date()), new Date()));
        } else if (intervention.getId().equals("RunningIntervention_Internal") && Rewards.runningCompleted(getApplicationContext())
            && !InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                .runningReferenceDateExists(DateUtils.minimizeMonthAndDay(new Date()).getTime())) {
            InterventionDatabase.getInstance(getApplicationContext()).rewardCompletionDao()
                    .insert(RewardCompletionEntity.createRunningEntity(DateUtils.minimizeMonthAndDay(new Date()), new Date()));
        }
    }

    private void showFeedbackAlertAndFinish(InterventionRecordEntity recordEntity) {
        LinearLayout linearLayout = new LinearLayout(InterventionActivity.this);
        InterventionFeedbackView interventionFeedbackView = new InterventionFeedbackView(InterventionActivity.this, null);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(64,64,64,64);
        interventionFeedbackView.setLayoutParams(marginLayoutParams);
        linearLayout.addView(interventionFeedbackView);
        new MaterialAlertDialogBuilder(InterventionActivity.this)
                .setView(linearLayout)
                .setPositiveButton(R.string.interventionDialogCommentBtn, (dialog, which) -> {
                    recordEntity.feedback = interventionFeedbackView.getComment();
                    recordEntity.rating = interventionFeedbackView.getRating();

                    AsyncTask.execute(() -> InterventionDatabase.getInstance(getApplicationContext())
                            .interventionRecordDao().update(recordEntity));

                    finish();
                })
                .setNeutralButton(R.string.interventionDialogSkipBtn, (dialog, which) -> {
                    finish();
                })
                .create()
                .show();
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

