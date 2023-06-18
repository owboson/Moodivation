package de.b08.moodivation.intervention;

import java.util.Collections;
import java.util.Map;

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

}
