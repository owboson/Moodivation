package de.b08.moodivation.questionnaire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.b08.moodivation.R;
import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.questionnaire.QuestionnaireElement;

public abstract class QuestionView<Q extends QuestionnaireElement,A extends Answer<?>> extends LinearLayout {

    private Q question;
    private String questionnaireId;

    private final TextView questionTextView;
    private final LinearLayout questionContentView;

    private List<QuestionUpdateEventHandler<Q,A>> updateHandlers;

    public QuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.question_view, this);
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionContentView = (LinearLayout) findViewById(R.id.questionContentView);
        updateHandlers = new ArrayList<>();
    }

    public void registerUpdateHandler(QuestionUpdateEventHandler<Q,A> handler) {
        updateHandlers.add(handler);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(false);
    }

    public void setQuestion(Q question) {
        if (question == null)
            return;

        this.question = question;
        getQuestionTextView().setText(question.getTitle());
    }


    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }


    public String getQuestionnaireId() {
        return questionnaireId;
    }


    public Q getQuestion() {
        return question;
    }

    public TextView getQuestionTextView() {
        return questionTextView;
    }

    public LinearLayout getQuestionContentView() {
        return questionContentView;
    }

    public abstract A getAnswer();

    public abstract boolean isAnswered();

    public void notifyUpdateHandlers() {
        updateHandlers.forEach(a -> a.handleQuestionAnswerUpdate(this));
    }
}
