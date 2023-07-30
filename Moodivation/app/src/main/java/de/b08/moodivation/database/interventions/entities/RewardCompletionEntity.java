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
import androidx.room.Entity;

import java.util.Date;
import java.util.Objects;

@Entity(primaryKeys = {"challenge", "referenceDate"})
public class RewardCompletionEntity {

    @NonNull
    public String challenge;

    @NonNull
    public Date completionDate;

    @NonNull
    public Date referenceDate;

    public RewardCompletionEntity(@NonNull String challenge, @NonNull Date referenceDate, @NonNull Date completionDate) {
        this.challenge = challenge;
        this.referenceDate = referenceDate;
        this.completionDate = completionDate;
    }

    public static RewardCompletionEntity createStreakEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("STREAK", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createStepsEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("STEPS", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createRunningEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("Running", referenceDate, completionDate);
    }

    public static RewardCompletionEntity createCyclingEntity(@NonNull Date referenceDate, @NonNull Date completionDate) {
        return new RewardCompletionEntity("Cycling", referenceDate, completionDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RewardCompletionEntity)) return false;
        RewardCompletionEntity that = (RewardCompletionEntity) o;
        return challenge.equals(that.challenge) && completionDate.equals(that.completionDate) && referenceDate.equals(that.referenceDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(challenge, completionDate, referenceDate);
    }
}
