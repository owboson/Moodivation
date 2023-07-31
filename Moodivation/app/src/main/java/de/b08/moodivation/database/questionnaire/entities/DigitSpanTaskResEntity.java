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

package de.b08.moodivation.database.questionnaire.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

@Entity(primaryKeys = {"timestamp"})
public class DigitSpanTaskResEntity {

    @NonNull
    public Date timestamp;

    @NonNull
    public Boolean afterNoonQuestionnaire;

    @NonNull
    public Integer result;

    @Nullable
    public Date questionnaireAnswerId;

    public DigitSpanTaskResEntity(@NonNull Date timestamp, @NonNull Boolean afterNoonQuestionnaire,
                                  @NonNull Integer result, @Nullable Date questionnaireAnswerId) {
        this.timestamp = timestamp;
        this.afterNoonQuestionnaire = afterNoonQuestionnaire;
        this.result = result;
        this.questionnaireAnswerId = questionnaireAnswerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DigitSpanTaskResEntity)) return false;
        DigitSpanTaskResEntity that = (DigitSpanTaskResEntity) o;
        return timestamp.equals(that.timestamp) && afterNoonQuestionnaire.equals(that.afterNoonQuestionnaire) && result.equals(that.result) && Objects.equals(questionnaireAnswerId, that.questionnaireAnswerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, afterNoonQuestionnaire, result, questionnaireAnswerId);
    }
}
