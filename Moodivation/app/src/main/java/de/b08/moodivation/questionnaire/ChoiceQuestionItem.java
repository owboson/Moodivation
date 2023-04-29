package de.b08.moodivation.questionnaire;

import java.util.Objects;

/**
 * 
 */
public class ChoiceQuestionItem {

    private final String value;
    private final String id;

    public ChoiceQuestionItem(String value, String id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoiceQuestionItem)) return false;
        ChoiceQuestionItem that = (ChoiceQuestionItem) o;
        return Objects.equals(value, that.value) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, id);
    }
}
