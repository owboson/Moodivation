package de.b08.moodivation.questionnaire;

import java.util.Objects;

/**
 * 
 */
public class QuestionnaireElement {

    private final String title;
    private final String id;

    public QuestionnaireElement(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireElement)) return false;
        QuestionnaireElement question = (QuestionnaireElement) o;
        return Objects.equals(title, question.title) && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id);
    }
}
