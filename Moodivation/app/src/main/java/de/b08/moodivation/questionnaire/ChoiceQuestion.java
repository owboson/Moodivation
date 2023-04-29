package de.b08.moodivation.questionnaire;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 
 */
public class ChoiceQuestion extends Question {

    private final boolean multiSelectionAllowed;
    private final List<ChoiceQuestionItem> items;
    private final String defaultItemId;

    public ChoiceQuestion(String title, String id, List<ChoiceQuestionItem> items,
                          boolean multiSelectionAllowed, String defaultItemId) {
        super(title, id);
        this.multiSelectionAllowed = multiSelectionAllowed;
        this.items = Collections.unmodifiableList(items);
        this.defaultItemId = defaultItemId;
    }

    public boolean isMultiSelectionAllowed() {
        return multiSelectionAllowed;
    }

    public List<ChoiceQuestionItem> getItems() {
        return items;
    }

    public Optional<String> getDefaultItemId() {
        return Optional.ofNullable(defaultItemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoiceQuestion)) return false;
        ChoiceQuestion that = (ChoiceQuestion) o;
        return multiSelectionAllowed == that.multiSelectionAllowed && Objects.equals(items, that.items)
                && Objects.equals(defaultItemId, that.defaultItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(multiSelectionAllowed, items, defaultItemId);
    }

}
