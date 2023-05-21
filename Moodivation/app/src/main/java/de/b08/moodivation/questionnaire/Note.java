package de.b08.moodivation.questionnaire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Note {

    @NonNull
    private final String questionnaireId;

    @Nullable
    private final String questionId;

    @Nullable
    private final String value;

    public Note(@NonNull String questionnaireId, @Nullable String questionId, @Nullable String value) {
        this.questionnaireId = questionnaireId;
        this.questionId = questionId;
        this.value = value;
    }

    @NonNull
    public String getQuestionnaireId() {
        return questionnaireId;
    }

    @Nullable
    public String getQuestionId() {
        return questionId;
    }

    @Nullable
    public String getValue() {
        return value;
    }

}
