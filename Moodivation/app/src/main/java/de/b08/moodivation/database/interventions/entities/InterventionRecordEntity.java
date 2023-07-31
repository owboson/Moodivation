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

package de.b08.moodivation.database.interventions.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(primaryKeys = {"interventionId", "startTimestamp", "endTimestamp"})
public class InterventionRecordEntity implements Serializable {

    @NonNull
    public String interventionId;

    /**
     * date when questionnaire was answered (null if intervention/activity was recorded without
     * being proposed by the app after a questionnaire)
     */
    @Nullable
    public Date questionnaireAnswerId;

    @NonNull
    public Date startTimestamp;

    @NonNull
    public Date endTimestamp;

    public boolean afterQuestionnaire;

    public Float rating;

    public String feedback;

    public InterventionRecordEntity(@NonNull String interventionId, @Nullable Date questionnaireAnswerId,
                                    @NonNull Date startTimestamp, @NonNull Date endTimestamp,
                                    boolean afterQuestionnaire, Float rating, String feedback) {
        this.interventionId = interventionId;
        this.questionnaireAnswerId = questionnaireAnswerId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.afterQuestionnaire = afterQuestionnaire;
        this.rating = rating;
        this.feedback = feedback;
    }

    public InterventionRecordEntity cloneEntity() {
        return new InterventionRecordEntity(interventionId, questionnaireAnswerId == null ? null : new Date(questionnaireAnswerId.getTime()),
                new Date(startTimestamp.getTime()), new Date(endTimestamp.getTime()), afterQuestionnaire, rating, feedback);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterventionRecordEntity)) return false;
        InterventionRecordEntity record = (InterventionRecordEntity) o;
        return afterQuestionnaire == record.afterQuestionnaire && interventionId.equals(record.interventionId) && Objects.equals(questionnaireAnswerId, record.questionnaireAnswerId) && startTimestamp.equals(record.startTimestamp) && endTimestamp.equals(record.endTimestamp) && Objects.equals(rating, record.rating) && Objects.equals(feedback, record.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interventionId, questionnaireAnswerId, startTimestamp, endTimestamp, afterQuestionnaire, rating, feedback);
    }
}
