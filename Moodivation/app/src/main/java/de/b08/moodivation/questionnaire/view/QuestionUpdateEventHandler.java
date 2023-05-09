package de.b08.moodivation.questionnaire.view;

import de.b08.moodivation.questionnaire.QuestionnaireElement;
import de.b08.moodivation.questionnaire.answer.Answer;

@FunctionalInterface
public interface QuestionUpdateEventHandler<Q extends QuestionnaireElement,A extends Answer<?>> {
    void handleQuestionAnswerUpdate(QuestionView<Q,A> view);
}
