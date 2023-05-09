package de.b08.moodivation.questionnaire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import de.b08.moodivation.R;
import de.b08.moodivation.questionnaire.answer.TextAnswer;
import de.b08.moodivation.questionnaire.question.FreeTextQuestion;

public class FreeTextQuestionView extends QuestionView<FreeTextQuestion, TextAnswer> {

    private final EditText answerTextField;

    public FreeTextQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.free_text_question_view, getQuestionContentView());
        answerTextField = (EditText) findViewById(R.id.freeTextQuestionAnswerTextField);
        answerTextField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                notifyUpdateHandlers();
        });
    }

    @Override
    public TextAnswer getAnswer() {
        return new TextAnswer(getQuestion().getId(), getQuestionnaireId(),
                isEnabled() ? answerTextField.getText().toString() : "");
    }

    @Override
    public boolean isAnswered() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (answerTextField != null)
            answerTextField.setEnabled(enabled);
    }

}
