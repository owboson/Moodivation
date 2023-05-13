package de.b08.moodivation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.Map;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;

public class MainActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuestionnaireDatabase.getInstance(getApplicationContext());

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        preset_sharedPreferences();

        Button digitSpanTaskPreviewBtn = findViewById(R.id.digitSpanTaskPreviewButton);
        digitSpanTaskPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, DigitSpanTask.class));
        });

        Button questionnairePreviewBtn = findViewById(R.id.questionnairePreviewButton);

        questionnairePreviewBtn.setOnClickListener(v -> {
            if (sharedPreferences.getInt("allow_questionnaire_data_collection", 0) == 1) {
                Intent questionnaireIntent = new Intent(this, QuestionnaireActivity.class);
                questionnaireIntent.putExtra("name", "main");
                MainActivity.this.startActivity(questionnaireIntent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Allow data collection to be able to complete the questionnaire")
                        .setTitle("Settings")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        Button settingsPreviewBtn = findViewById(R.id.settingsPreviewButton);
        settingsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, SettingsPage.class));
        });
    }

    private void preset_sharedPreferences() {
        if (!sharedPreferences.contains("morning_from")) {
            editor.putString("morning_from", "9:0");
        }
        if (!sharedPreferences.contains("morning_to")) {
            editor.putString("morning_to", "10:30");
        }
        if (!sharedPreferences.contains("day_from")) {
            editor.putString("day_from", "13:30");
        }
        if (!sharedPreferences.contains("day_to")) {
            editor.putString("day_to", "14:30");
        }
        if (!sharedPreferences.contains("evening_from")) {
            editor.putString("evening_from", "18:0");
        }
        if (!sharedPreferences.contains("evening_to")) {
            editor.putString("evening_to", "19:30");
        }
        if (!sharedPreferences.contains("allow_data_collection")) {
            editor.putInt("allow_data_collection", 0);
        }
        if (!sharedPreferences.contains("google_fit_collection")) {
            editor.putInt("google_fit_collection", 0);
        }
        if (!sharedPreferences.contains("allow_notifs")) {
            editor.putInt("allow_notifs", 0);
        }
        if (!sharedPreferences.contains("allow_questionnaire_data_collection")) {
            editor.putInt("allow_questionnaire_data_collection", 1);
        }
        if (!sharedPreferences.contains("questionnaire_periodicity")) {
            editor.putInt("questionnaire_periodicity", 3);
        }
        if (!sharedPreferences.contains("questionnaire_periodicity_details")) {
            editor.putInt("questionnaire_periodicity", 0);
        }
        if (!sharedPreferences.contains("allow_digit_span_collection")) {
            editor.putInt("allow_digit_span_collection", 1);
        }

        Map<String, ?> allValues = sharedPreferences.getAll();

// Iterate over the map and print out the key-value pairs
        for (Map.Entry<String, ?> entry : allValues.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Log.d("TAG", key + ": " + value.toString());
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}