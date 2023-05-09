package de.b08.moodivation.questionnaire.answer;

/**
 * 
 */
public abstract class Answer<T> {

    private final T value;
    private final String questionId;
    private final String questionnaireId;

    public Answer(String questionId, String questionnaireId, T value) {
        this.value = value;
        this.questionId = questionId;
        this.questionnaireId = questionnaireId;
    }

    public T getValue() {
        return value;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

}
