package de.b08.moodivation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.color.MaterialColors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.database.sensors.SensorDatabase;
import de.b08.moodivation.database.sensors.dao.StepDao;
import de.b08.moodivation.intervention.Intervention;
import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;
import de.b08.moodivation.ui.ShareButton;
import de.b08.moodivation.utils.ShareUtils;

public class Rewards extends Fragment {

    InterventionDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_rewards, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = InterventionDatabase.getInstance(getContext());

        AsyncTask.execute(() -> {

            List<Integer> imageList = new ArrayList<>();
            List<String> textList = new ArrayList<>();
            List<String> shareMessages = new ArrayList<>();

            // get streak, running and cycling data of this month
            int[] data = getData(getContext());

            // add streak reward to list (if exists)
            int streak = data[0];
            if (streak == 0) {
                imageList.add(R.drawable.streak_reward_img_grey);
                textList.add(getResources().getString(R.string.notWorkoutStreak));
                shareMessages.add(null);
            } else if (streak >= 1){
                imageList.add(R.drawable.streak_reward_img);
                if (streak == 1) {
                    shareMessages.add(getResources().getString(R.string.workoutStreakSharedMessage));
                    textList.add(getResources().getString(R.string.workoutStreak).replace("%NUM%", Integer.toString(streak)));
                } else {
                    shareMessages.add(getResources().getString(R.string.workoutsStreakSharedMessage).replace("%NUM%", "" + streak));
                    textList.add(getResources().getString(R.string.workoutStreakPlural).replace("%NUM%", Integer.toString(streak)));
                }
            }

            int steps = get5000Steps(getContext());
            if (steps >= 5000) {
                shareMessages.add(getResources().getString(R.string.moreStepsSharedMessage).replace("%NUM%", "" + 5000));
                imageList.add(R.drawable.steps_reward_img);
                textList.add(getResources().getString(R.string.walkedSteps).replace("%NUM%", Integer.toString(steps)));
            } else {
                if (steps != 0)
                    shareMessages.add(getResources().getString(R.string.stepsSharedMessage).replace("%NUM%", "" + steps));
                else
                    shareMessages.add(null);
                imageList.add(R.drawable.steps_reward_img_grey);
                textList.add(getResources().getString(R.string.notEnoughSteps));
            }

            int running = data[1];
            if (running >= 10) {
                shareMessages.add(getResources().getString(R.string.moreRunsSharedMessage).replace("%NUM%", "" + 10));
                imageList.add(R.drawable.running_reward_img);
                textList.add(getResources().getString(R.string.moreRuns));
            } else {
                if (running != 0)
                    shareMessages.add(getResources().getString(R.string.runsSharedMessage).replace("%NUM%", "" + running));
                else
                    shareMessages.add(null);
                imageList.add(R.drawable.running_reward_img_grey);
                textList.add(running + getResources().getText(R.string.runsLeft).toString().replace("%NUM%", Integer.toString(10 - running)));
            }

            int cycling = data[2];
            if (cycling >= 10) {
                shareMessages.add(getResources().getString(R.string.moreBikeRidesSharedMessage).replace("%NUM%", "" + 10));
                imageList.add(R.drawable.cycling_reward_img);
                textList.add(getResources().getString(R.string.moreRides));
            } else {
                if (cycling != 0)
                    shareMessages.add(getResources().getString(R.string.bikeRidesSharedMessage).replace("%NUM%", "" + cycling));
                else
                    shareMessages.add(null);

                imageList.add(R.drawable.cycling_reward_img_grey);
                textList.add(cycling+ getResources().getText(R.string.workoutsLeft).toString().replace("%NUM%", Integer.toString(10 - cycling)));
            }

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> create_view(imageList, textList, shareMessages, getContext(), view));

        });
    }

    public static int get5000Steps(Context context){
        SensorDatabase sensorDatabase = SensorDatabase.getInstance(context);
        StepDao stepDao = sensorDatabase.stepDataDao();

        Calendar calendar = Calendar.getInstance();

// Set time to the beginning of the day (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

// Get the start timestamp of today as a long value
        long startTimestamp = calendar.getTimeInMillis();

// Set time to the end of the day (23:59:59)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

// Get the end timestamp of today as a long value
        long endTimestamp = calendar.getTimeInMillis();

        return stepDao.getStepsInTimeInterval(startTimestamp, endTimestamp);
    }

    public static int[] getData(Context context) {
        final List<InterventionRecordEntity> records = InterventionDatabase.getInstance(context).interventionRecordDao().getAllRecords();
        Collections.reverse(records);

        int running = 0;
        int cycling = 0;

        List<LocalDate> dateList = new ArrayList<>();
        String id = "";

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        for (final InterventionRecordEntity record : records) {
            final Optional<InterventionBundle> interventionBundle = InterventionLoader.getInterventionWithId(record.interventionId, context);
            if (interventionBundle.isPresent()) {
                InterventionBundle bundle = interventionBundle.get();
                // Access the intervention or intervention map from the bundle
                Intervention intervention = bundle.getInterventionMap().values().stream().findFirst().orElse(null);
                if (intervention != null) {
                    id = intervention.getId();
                }
            }


            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            try {
                Date parsedDate = inputFormat.parse(String.valueOf(record.startTimestamp));
                LocalDate localDate = parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dateList.add(localDate);

                boolean isWithinRange = (localDate.isEqual(firstDayOfMonth) || localDate.isAfter(firstDayOfMonth)) &&
                        (localDate.isEqual(lastDayOfMonth) || localDate.isBefore(lastDayOfMonth));
                if (isWithinRange) {
                    if (id.equals("RunningIntervention_Internal")) {
                        running+=1;
                    } else if (id.equals("CyclingIntervention_Internal")) {
                        cycling+=1;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        int streak=0;
        while(dateList.contains(today)){
            streak+=1;
            today = today.minusDays(1);
        }
        int[] result = new int[3];
        result[0] = streak;
        result[1] = running;
        result[2] = cycling;

        return result;
    }

    void create_view(List<Integer> imageList, List<String> textList, List<String> shareMessages, Context context, View view) {
        LinearLayout containerLayout = view.findViewById(R.id.containerLayout);

        for (int i = 0; i < imageList.size(); i++) {
            // Create CardView
            CardView cardView = new CardView(context);
            cardView.setCardBackgroundColor(MaterialColors.getColor(cardView, com.google.android.material.R.attr.colorSecondaryContainer));

            int paddingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_size);
            cardView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

            // Set CardView properties as needed
            cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));

            // Create a LinearLayout as the content container
            LinearLayout contentLayout = new LinearLayout(getContext());
            contentLayout.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentLayout.setOrientation(LinearLayout.VERTICAL);
            contentLayout.setGravity(Gravity.CENTER);

            // Create ImageView
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(350, 350);
            imageView.setLayoutParams(imageParams);
            imageView.setImageResource((int) imageList.get(i));

            // Create TextView
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(textParams);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER);
            textView.setText((String )textList.get(i));
            textView.setPadding(5, 5, 5, 15);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // Set text size to 30dp
            textView.setTypeface(null, Typeface.BOLD); // Set text to bold
            textView.setTextColor(MaterialColors.getColor(textView, com.google.android.material.R.attr.colorOnSecondaryContainer));

            // Add ImageView and TextView to contentLayout
            contentLayout.addView(imageView);
            contentLayout.addView(textView);

            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.addView(contentLayout);
            ShareButton shareButton = new ShareButton(getContext());

            String shareMessage = shareMessages.get(i);
            boolean enabled = shareMessage != null;

            shareButton.setEnabled(enabled);
            shareButton.setVisibility(enabled ? View.VISIBLE : View.GONE);
            shareButton.getShareBtn().setOnClickListener(v -> ShareUtils.shareTextIntent(shareMessage, getActivity()));

            frameLayout.addView(shareButton);

            // Add frameLayout to CardView
            cardView.addView(frameLayout);

            // Add CardView to containerLayout
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(10, 30, 10, 30); // Optional: Set margins between card views
            cardView.setLayoutParams(cardParams);
            containerLayout.addView(cardView);
        }


    }

    public static boolean stepsCompleted(Context context) {
        return get5000Steps(context) >= 5000;
    }

    public static boolean cyclingCompleted(Context context) {
        return getData(context)[2] >= 10;
    }

    public static boolean runningCompleted(Context context) {
        return getData(context)[1] >= 10;
    }

    public static boolean streakCompleted(Context context) {
        return getData(context)[0] >= 1;
    }
}