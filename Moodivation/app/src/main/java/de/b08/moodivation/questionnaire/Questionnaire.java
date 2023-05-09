package de.b08.moodivation.questionnaire;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.b08.moodivation.questionnaire.question.Question;

/**
 * 
 */
public class Questionnaire {

    private final List<Question> questions;
    private final String title;
    private final String id;

    public Questionnaire(String title, String id, List<Question> questions) {
        this.title = title;
        this.questions = questions;
        this.id = id;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Questionnaire)) return false;
        Questionnaire that = (Questionnaire) o;
        return Objects.equals(id, that.id) && Objects.equals(questions, that.questions)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions, title, id);
    }

}
