package de.b08.moodivation.intervention;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @Nullable
    private final Set<DataType> collectedDataTypes;

    public Intervention(@NonNull String id, @NonNull String title, @Nullable String description,
                        @Nullable InterventionMedia interventionMedia, @Nullable List<DataType> collectedDataTypes,
                        @Nullable InterventionOptions options) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.interventionMedia = interventionMedia;
        this.options = options == null ? InterventionOptions.getDefaultOptions() : options;
        this.collectedDataTypes = collectedDataTypes == null ? null : new HashSet<>(collectedDataTypes);
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
    public Set<DataType> getCollectedDataTypes() {
        return collectedDataTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intervention)) return false;
        Intervention that = (Intervention) o;
        return id.equals(that.id) && title.equals(that.title) && Objects.equals(description, that.description) && Objects.equals(interventionMedia, that.interventionMedia) && options.equals(that.options) && Objects.equals(collectedDataTypes, that.collectedDataTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, interventionMedia, options, collectedDataTypes);
    }
}
