package de.b08.moodivation.questionnaire;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QuestionnaireBundle {

    private final String name;
    private final Map<String, Questionnaire> questionnaires;

    public QuestionnaireBundle(String name, Map<String, Questionnaire> questionnaires) {
        this.name = name;
        this.questionnaires = questionnaires;
    }

    public Questionnaire getQuestionnaire(String lang) {
        if (questionnaires.containsKey(lang))
            return questionnaires.get(lang);

        return questionnaires.containsKey("en") ? questionnaires.get("en") : questionnaires.values().stream().findFirst().orElse(null);
    }

    public Set<String> getLanguages() {
        return questionnaires.keySet();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireBundle)) return false;
        QuestionnaireBundle that = (QuestionnaireBundle) o;
        return Objects.equals(name, that.name) && Objects.equals(questionnaires, that.questionnaires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, questionnaires);
    }

}
