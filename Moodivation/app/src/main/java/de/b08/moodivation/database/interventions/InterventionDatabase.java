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

package de.b08.moodivation.database.interventions;

import android.content.Context;

import androidx.room.BuiltInTypeConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.b08.moodivation.database.interventions.dao.RewardCompletionDao;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;
import de.b08.moodivation.database.interventions.dao.InterventionRecordDao;
import de.b08.moodivation.database.interventions.entities.RewardCompletionEntity;
import de.b08.moodivation.database.typeconverter.DateConverter;


@Database(entities = {InterventionRecordEntity.class, RewardCompletionEntity.class},
        exportSchema = true,
        version = 3)
@TypeConverters(value = {DateConverter.class},
        builtInTypeConverters = @BuiltInTypeConverters(enums = BuiltInTypeConverters.State.ENABLED))
public abstract class InterventionDatabase extends RoomDatabase {

    private static volatile InterventionDatabase instance = null;

    public static InterventionDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (InterventionDatabase.class) {
                instance = Room.databaseBuilder(context, InterventionDatabase.class,
                                "InterventionDatabase").fallbackToDestructiveMigration()
                        .build();
                return instance;
            }
        }
        return instance;
    }

    public abstract InterventionRecordDao interventionRecordDao();

    public abstract RewardCompletionDao rewardCompletionDao();

}
