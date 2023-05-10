package de.b08.moodivation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.b08.moodivation.database.questionnaire.QuestionnaireDatabase;
import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;
import de.b08.moodivation.questionnaire.QuestionnaireBundle;
import de.b08.moodivation.questionnaire.QuestionnaireLoader;
import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.questionnaire.view.QuestionnaireView;

public class QuestionnaireActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        QuestionnaireView questionnaireView = findViewById(R.id.questionnaireView);
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            saveAnswers(questionnaireView.getAllAnswers());

            String day_from = sharedPreferences.getString("day_from", "13:30");
            String day_to = sharedPreferences.getString("day_to", "14:30");
            if (isNoonQuestionnaire(new Date(), day_from, day_to)) {
                Intent digitSpanTask = new Intent(this, DigitSpanTask.class);
                digitSpanTask.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                digitSpanTask.putExtra("afterNoonQuestionnaire", true);
                startActivity(digitSpanTask);
            }
        });

        questionnaireView.addUpdateHandler(() -> saveBtn.setEnabled(questionnaireView.isAnswered()));

        String questionnaireName = getIntent().hasExtra("name") ? getIntent().getStringExtra("name") : "main";
        try {
            QuestionnaireBundle questionnaireBundle = QuestionnaireLoader.loadFromAssets(getApplicationContext()).get(questionnaireName);
            questionnaireView.setQuestionnaire(questionnaireBundle.getQuestionnaire(questionnaireBundle.getLanguages().stream().findFirst().get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAnswers(List<Answer<?>> answers) {
        Date now = new Date();
        List<AnswerEntity> answerEntities = answers.stream()
                .map(a -> AnswerEntity.from(a, now))
                .collect(Collectors.toList());

        AsyncTask.execute(() -> QuestionnaireDatabase.getInstance(getApplicationContext()).answerDao().insertAll(answerEntities));
    }

    private boolean isNoonQuestionnaire(Date now, String from, String to) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date dateDayFrom = dateFormat.parse(from);
            Date dateDayTo = dateFormat.parse(to);
            Date dateNow = dateFormat.parse(dateFormat.format(now));

            return dateNow.equals(dateDayTo) ||dateNow.equals(dateDayFrom) || dateNow.after(dateDayFrom) && dateNow.before(dateDayTo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}