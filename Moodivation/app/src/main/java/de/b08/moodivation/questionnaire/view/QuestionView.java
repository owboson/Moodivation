package de.b08.moodivation.questionnaire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import de.b08.moodivation.R;
import de.b08.moodivation.questionnaire.Note;
import de.b08.moodivation.questionnaire.QuestionnaireElement;
import de.b08.moodivation.questionnaire.answer.Answer;

public abstract class QuestionView<Q extends QuestionnaireElement,A extends Answer<?>> extends LinearLayout {

    private Q question;
    private String questionnaireId;

    private final TextView questionTextView;
    private final LinearLayout questionContentView;
    private final ImageButton addNoteButton;

    private String note;

    private List<QuestionUpdateEventHandler<Q,A>> updateHandlers;

    public QuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.question_view, this);
        questionTextView = findViewById(R.id.questionTextView);
        questionContentView = findViewById(R.id.questionContentView);
        addNoteButton = findViewById(R.id.addNoteButton);
        updateHandlers = new ArrayList<>();

        note = null;
        addNoteButton.setOnClickListener(v -> showAddNoteAlert());
    }

    private void showAddNoteAlert() {
        QuestionnaireNotesView questionnaireNotesView = new QuestionnaireNotesView(getContext(), null);
        if (note != null)
            questionnaireNotesView.setText(note);

        questionnaireNotesView.getQuestionnaireNotesTextBox().setMinimumHeight(450);
        questionnaireNotesView.setTitleVisible(false);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle(getResources().getString(R.string.addQuestionNoteAlertTitle))
                .setView(questionnaireNotesView)
                .setPositiveButton("OK", (dialog, which) -> {
                    note = questionnaireNotesView.getText().toString();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .show();
    }

    public void registerUpdateHandler(QuestionUpdateEventHandler<Q,A> handler) {
        updateHandlers.add(handler);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
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

    @NonNull
    public Note getNote() {
        // TODO: add proper isBlank
        String val = note == null ? null : note.trim().isEmpty() ? null : note;
        return new Note(questionnaireId, question.getId(), val);
    }
}
