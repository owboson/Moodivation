package de.b08.moodivation.questionnaire;

import java.util.Objects;

/**
 * 
 */
public class FreeTextQuestion extends Question {

    private final boolean multiLineAllowed;

    public FreeTextQuestion(String title, String id, boolean multiLineAllowed) {
        super(title, id);
        this.multiLineAllowed = multiLineAllowed;
    }

    public boolean isMultiLineAllowed() {
        return multiLineAllowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreeTextQuestion)) return false;
        if (!super.equals(o)) return false;
        FreeTextQuestion that = (FreeTextQuestion) o;
        return multiLineAllowed == that.multiLineAllowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), multiLineAllowed);
    }

}
