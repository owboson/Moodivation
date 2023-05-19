package de.b08.moodivation.sensors;

public class Observation {

    private float[] values;
    private int accuracy;
    private long timestamp;

    public Observation(float[] values, int accuracy, long timestamp) {
        this.values = values;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
    }

    public float[] getValues() {
        return values;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAccuracy() {
        return accuracy;
    }

}
