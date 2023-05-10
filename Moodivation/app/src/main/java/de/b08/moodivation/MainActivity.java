package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;

public class MainActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuestionnaireDatabase.getInstance(getApplicationContext());

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        Button digitSpanTaskPreviewBtn = findViewById(R.id.digitSpanTaskPreviewButton);
        digitSpanTaskPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, DigitSpanTask.class));
        });

        Button questionnairePreviewBtn = findViewById(R.id.questionnairePreviewButton);
        questionnairePreviewBtn.setOnClickListener(v -> {
            Intent questionnaireIntent = new Intent(this, QuestionnaireActivity.class);
            questionnaireIntent.putExtra("name", "main");
            MainActivity.this.startActivity(questionnaireIntent);
        });

        Button settingsPreviewBtn = findViewById(R.id.settingsPreviewButton);
        settingsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(this, SettingsPage.class));
        });
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}