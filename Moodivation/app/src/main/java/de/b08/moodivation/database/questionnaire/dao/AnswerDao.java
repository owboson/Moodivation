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

package de.b08.moodivation.database.questionnaire.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import de.b08.moodivation.database.questionnaire.entities.AnswerEntity;

@Dao
public interface AnswerDao {

    @Query("SELECT * FROM AnswerEntity WHERE AnswerEntity.questionId = :questionId AND AnswerEntity.questionnaireId = :questionnaireId")
    List<AnswerEntity> getAllAnswersForQuestion(String questionnaireId, String questionId);

    @Insert
    void insert(AnswerEntity entity);

    @Insert
    void insertAll(List<AnswerEntity> entities);

    @Query("SELECT DISTINCT(timestamp) FROM AnswerEntity")
    List<Date> getAllDates();

    @Query("SELECT * FROM AnswerEntity WHERE timestamp = :timestamp")
    List<AnswerEntity> getAllAnswerEntitiesWithTimestamp(long timestamp);

}
