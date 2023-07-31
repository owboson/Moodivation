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

package de.b08.moodivation.intervention;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Stores an intervention in different languages
 */
public class InterventionBundle {

    private final Map<String, Intervention> interventionMap;
    private final String id;

    public InterventionBundle(String id, Map<String, Intervention> interventionMap) {
        this.id = id;
        this.interventionMap = Collections.unmodifiableMap(interventionMap);
    }

    public Map<String, Intervention> getInterventionMap() {
        return interventionMap;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterventionBundle)) return false;
        InterventionBundle that = (InterventionBundle) o;
        return Objects.equals(interventionMap, that.interventionMap) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interventionMap, id);
    }
}
