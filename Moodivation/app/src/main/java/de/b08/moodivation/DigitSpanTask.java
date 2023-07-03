package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.database.questionnaire.entities.DigitSpanTaskResEntity;
import de.b08.moodivation.intervention.InterventionLoader;

@SuppressWarnings("FieldCanBeLocal")
public class DigitSpanTask extends AppCompatActivity {
//   text field that shows the number on the screnn
    private TextView numberField;
//    text field that shows instructions, congratulation/wrong number prompt
    private TextView instructionField;
//    start button at the bottom of the page
    private Button startBtn;
//    maximum number of numbers user could repeat in a row
    private int userMax = 0;
//    current upper bound of numbers to be shown one after another;
//    after each correct answer is increased by 1
    private int upperBound = 2;

    private int mCount = 0;
    Random rand = new Random();
    private int mCurrentIndex = 0;
    private int[] mNumbers = new int[upperBound];

    // all of our buttons from 0 to 9
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;

    // array of those buttons
    ArrayList<Button> buttons = new ArrayList<>();

    protected SharedPreferences sharedPreferences;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
//  show numbers one by one on the screen with a pause of 1 second between each number
            mCount++;
            if (mCount <= upperBound) {
                int randomNumber = rand.nextInt(10);
                System.out.println("random "+randomNumber);
                mNumbers[mCount-1] = randomNumber;
                numberField.setText(String.valueOf(randomNumber));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numberField.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(() -> {
                                numberField.setVisibility(View.INVISIBLE);
                                mHandler.postDelayed(mRunnable, 1000);
                            }, 1000);
                        }
                    }, 1000);
            } else {
                // Show message that user should start entering numbers
                instructionField.setText(R.string.digitSpanEnterNumbersInstruction);
                mCurrentIndex = 0;
                enableButtons();
            }

        }
    };

    // disable buttons from 0 to 9
    private void disableButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            ((Button)buttons.get(i)).setEnabled(false);
        }
    }

    // enable buttons from 0 to 9
    private void enableButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            ((Button)buttons.get(i)).setEnabled(true);
        }
    }

    // parse pressed buttons and compare them to list of shown numbers
    private final View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            int clickedNumber = Integer.parseInt(button.getText().toString());
            if (clickedNumber == mNumbers[mCurrentIndex]) {
                numberField.setText(String.valueOf(clickedNumber));
                mCurrentIndex++;
                if (mCurrentIndex == upperBound) {
                    // Show message that user has successfully completed the task
                    instructionField.setText(R.string.digitSpanCongrats);
                    userMax = upperBound;
                    upperBound+=1;
                    disableButtons();
                }
            } else {
                // Show message that user made a mistake
                instructionField.setText(getResources().getText(R.string.digitSpanFinalRes).toString().replace("%NUM%", "" + userMax));

                // store in database
                saveResult();

                disableButtons();
                startBtn.setEnabled(false);
            }
        }
    };

    private void saveResult() {

        long questionnaireAnswerId = getIntent().getLongExtra("questionnaireAnswerId", -1);
        if (sharedPreferences.getInt("allow_digit_span_collection", 1) == 1) {
            boolean afterNoonQuestionnaire = getIntent().hasExtra("afterNoonQuestionnaire") ?
                    getIntent().getExtras().getBoolean("afterNoonQuestionnaire", false) : false;
            AsyncTask.execute(() -> {
                QuestionnaireDatabase.getInstance(getApplicationContext()).digitSpanTaskResDao()
                        .insert(new DigitSpanTaskResEntity(new Date(), afterNoonQuestionnaire, userMax,
                                questionnaireAnswerId == -1 ? null : new Date(questionnaireAnswerId)));
            });
        }

        boolean presentIntervention = getIntent().hasExtra("presentIntervention") ?
                getIntent().getExtras().getBoolean("presentIntervention") : false;
        if (presentIntervention) {
            Intent interventionIntent = new Intent(this, InterventionActivity.class);
            if (questionnaireAnswerId != -1)
                interventionIntent.putExtra("questionnaireAnswerId", questionnaireAnswerId);
            interventionIntent.putExtra("afterQuestionnaire", true);
            interventionIntent.putExtra(InterventionActivity.INTERVENTION_EXTRA_KEY,
                    InterventionLoader.getLocalizedIntervention(InterventionLoader.getRandomIntervention(getApplicationContext())));

            startActivity(interventionIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digit_span_task);
        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("allow_digit_span_collection", 1) == 0) {
            new MaterialAlertDialogBuilder(DigitSpanTask.this)
                    .setTitle("Settings")
                    .setMessage("Allow data collection in the settings to save results.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Perform action when "Discard" button is clicked
                        }
                    })
                    .show();
        }
            // fields setup
        this.numberField = findViewById(R.id.numberField);
        this.startBtn = findViewById(R.id.start);
        this.instructionField = findViewById(R.id.instructionField);
        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);

        // set buttons to the same click listener
        button0.setOnClickListener(mButtonClickListener);
        button1.setOnClickListener(mButtonClickListener);
        button2.setOnClickListener(mButtonClickListener);
        button3.setOnClickListener(mButtonClickListener);
        button4.setOnClickListener(mButtonClickListener);
        button5.setOnClickListener(mButtonClickListener);
        button6.setOnClickListener(mButtonClickListener);
        button7.setOnClickListener(mButtonClickListener);
        button8.setOnClickListener(mButtonClickListener);
        button9.setOnClickListener(mButtonClickListener);

        // add buttons to the array list of all buttons for easy enabling and disabling
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);

        disableButtons();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionField.setText(R.string.digitSpanMemorizeInstruction);
                mCount = 0; // Reset the count
                mNumbers = new int[upperBound]; // reset the array of shown numbers
                mHandler.postDelayed(mRunnable, 1000); // Start the countdown
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable); // Stop the countdown when the activity is destroyed
    }


}