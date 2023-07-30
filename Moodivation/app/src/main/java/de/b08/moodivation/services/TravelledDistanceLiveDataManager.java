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

package de.b08.moodivation.services;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class TravelledDistanceLiveDataManager {

    private static TravelledDistanceLiveDataManager instance;
    private final List<TravelledDistanceLiveData> liveDataObjects;

    public static TravelledDistanceLiveDataManager getInstance() {
        if (instance == null) {
            instance = new TravelledDistanceLiveDataManager();
        }

        return instance;
    }

    private TravelledDistanceLiveDataManager() {
        liveDataObjects = new ArrayList<>();
    }

    public static class TravelledDistanceLiveData extends LiveData<Double> {

        private TravelledDistanceLiveData() {}

        public void addDistance(Double val) {
            double current = getValue() == null ? 0 : getValue();
            postValue(current + val);
        }

        public void reset() {
            postValue(0D);
        }

    }

    public TravelledDistanceLiveData create() {
        TravelledDistanceLiveData travelledDistanceLiveData = new TravelledDistanceLiveData();
        liveDataObjects.add(travelledDistanceLiveData);
        return travelledDistanceLiveData;
    }

    public void addDistance(Double val) {
        liveDataObjects.forEach(l -> l.addDistance(val));
    }

}
