/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.b08.moodivation.questionnaire;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Bundles questionnaire of different languages
 */
public class QuestionnaireBundle {

    private final String name;
    private final Map<String, Questionnaire> questionnaires;

    public QuestionnaireBundle(String name, Map<String, Questionnaire> questionnaires) {
        this.name = name;
        this.questionnaires = questionnaires;
    }

    /**
     * Returns the questionnaire in the given language if available.
     * @param lang the preferred language
     * @return Returns the questionnaire in the given language if available or the english questionnaire if available or null.
     */
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
