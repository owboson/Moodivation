package de.b08.moodivation.questionnaire;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.b08.moodivation.questionnaire.answer.Answer;
import de.b08.moodivation.questionnaire.answer.NumberAnswer;

public class SimpleWellbeingAlgorithm implements WellbeingAlgorithm {

    @Override
    public double calculateWellbeing(List<Answer<?>> questionnaireAnswers) {
        // only takes well/unwell question into account (question id 1.3 of questionnaire 1)
        Optional<Answer<?>> wellbeingAnswer = questionnaireAnswers.stream()
                .filter(a -> Objects.equals(a.getQuestionId(), "1.3") && Objects.equals(a.getQuestionnaireId(), "1"))
                .findFirst();

        if (wellbeingAnswer.isPresent()) {
            if (wellbeingAnswer.get() instanceof NumberAnswer) {
                return ((NumberAnswer) wellbeingAnswer.get()).getValue() / 100;
            }
        }
        return 0;
    }

    @Override
    public boolean shouldPresentIntervention(List<Answer<?>> questionnaireAnswers) {
        return calculateWellbeing(questionnaireAnswers) >= 0;
    }

}
