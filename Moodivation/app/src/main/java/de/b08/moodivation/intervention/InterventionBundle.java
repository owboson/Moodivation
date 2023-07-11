package de.b08.moodivation.intervention;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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
