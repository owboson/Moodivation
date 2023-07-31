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

package de.b08.moodivation.database.sensors.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.b08.moodivation.database.sensors.entities.StepDataEntity;

@Dao
public interface StepDao {

    @Insert
    void insert(StepDataEntity entity);

    @Insert
    void insertAll(List<StepDataEntity> entities);

    @Delete
    void delete(StepDataEntity entity);

    @Delete
    void deleteAll(List<StepDataEntity> entities);

    @Query("SELECT * FROM StepDataEntity WHERE timestamp >= :from AND timestamp <= :to")
    List<StepDataEntity> getAllEntitiesInTimeInterval(long from, long to);

    @Query("SELECT COUNT(*) FROM StepDataEntity WHERE timestamp >= :from AND timestamp <= :to")
    int getStepsInTimeInterval(long from, long to);

}
