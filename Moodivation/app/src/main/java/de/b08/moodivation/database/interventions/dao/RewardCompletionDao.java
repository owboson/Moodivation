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

package de.b08.moodivation.database.interventions.dao;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;

@Dao
public interface RewardCompletionDao {

    @Insert
    void insert(RewardCompletionEntity entity);

    @Query("SELECT COUNT(*) FROM RewardCompletionEntity WHERE challenge = :challenge AND referenceDate = :referenceDate")
    int referenceDateExists(String challenge, long referenceDate);

    @Ignore
    default boolean cyclingReferenceDateExists(long referenceDate) {
        return referenceDateExists("Cycling", referenceDate) != 0;
    }

    @Ignore
    default boolean runningReferenceDateExists(long referenceDate) {
        return referenceDateExists("Running", referenceDate) != 0;
    }

    @Ignore
    default boolean stepsReferenceDateExists(long referenceDate) {
        return referenceDateExists("STEPS", referenceDate) != 0;
    }

    @Ignore
    default boolean streakReferenceDateExists(long referenceDate) {
        return referenceDateExists("STREAK", referenceDate) != 0;
    }

}
