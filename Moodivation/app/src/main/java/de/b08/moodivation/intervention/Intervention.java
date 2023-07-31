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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Class for an intervention
 */
public class Intervention implements Serializable {

    public enum DataType {
        ELAPSED_TIME, TRAVELLED_DISTANCE, SPEED, STEPS
    }

    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @Nullable
    private final String description;

    @Nullable
    private final InterventionMedia interventionMedia;

    @NonNull
    private final InterventionOptions options;

    /**
     * The data types that should be displayed in the UI
     */
    @Nullable
    private final List<DataType> collectedDataTypes;

    public Intervention(@NonNull String id, @NonNull String title, @Nullable String description,
                        @Nullable InterventionMedia interventionMedia, @Nullable List<DataType> collectedDataTypes,
                        @Nullable InterventionOptions options) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.interventionMedia = interventionMedia;
        this.options = options == null ? InterventionOptions.getDefaultOptions() : options;
        this.collectedDataTypes = collectedDataTypes;
    }

    public Intervention(@NonNull String id, @NonNull String title, @Nullable String description,
                        @Nullable InterventionMedia interventionMedia, @Nullable List<DataType> collectedDataTypes) {
        this(id, title, description, interventionMedia, collectedDataTypes, null);
    }

    public @NonNull String getTitle() {
        return title;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public @Nullable InterventionMedia getInterventionMedia() {
        return interventionMedia;
    }

    public @NonNull InterventionOptions getOptions() {
        return options;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public List<DataType> getCollectedDataTypes() {
        return collectedDataTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intervention)) return false;
        Intervention that = (Intervention) o;
        return id.equals(that.id) && title.equals(that.title) && Objects.equals(description, that.description) && Objects.equals(interventionMedia, that.interventionMedia) && options.equals(that.options) && Objects.equals(collectedDataTypes == null ? null : new HashSet<>(collectedDataTypes), that.collectedDataTypes == null ? null : new HashSet<>(that.collectedDataTypes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, interventionMedia, options, collectedDataTypes);
    }
}
