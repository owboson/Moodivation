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

package de.b08.moodivation.sensors;

import android.hardware.SensorManager;

import java.util.Arrays;

public enum SensorDelay {

    FASTEST(SensorManager.SENSOR_DELAY_FASTEST),
    UI(SensorManager.SENSOR_DELAY_UI),
    GAME(SensorManager.SENSOR_DELAY_GAME),
    NORMAL(SensorManager.SENSOR_DELAY_NORMAL);

    private final int value;

    /**
     * @param val Either set to one of {@link SensorManager#SENSOR_DELAY_NORMAL}, {@link SensorManager#SENSOR_DELAY_UI},
     *            {@link SensorManager#SENSOR_DELAY_GAME}, {@link SensorManager#SENSOR_DELAY_FASTEST} or the delay
     *            in milliseconds.
     */
    SensorDelay(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }

    public static SensorDelay getDelayFor(int value) {
        return Arrays.stream(values()).filter(d -> d.value == value).findFirst().orElse(FASTEST);
    }

}
