package de.b08.moodivation.questionnaire;

import java.util.List;

import de.b08.moodivation.questionnaire.answer.Answer;

public interface WellbeingAlgorithm {

    WellbeingAlgorithm INSTANCE = new SimpleWellbeingAlgorithm();

    double calculateWellbeing(List<? extends Answer<?>> questionnaireAnswers);

    boolean shouldPresentIntervention(List<Answer<?>> questionnaireAnswers);

}
