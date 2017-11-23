package scene;
import engine.*;
// Represents a configuration of a hand

public class HandConfiguration implements Interpolatable {

    public final static int NUM_FINGERS = 4;
    public final static int NUM_FINGER_TWEAKS = 6;

    private float[][] fingerValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];

    public HandConfiguration() {
    }

    public HandConfiguration(float[][] values) {
        fingerValues = values;
    }

    public float[][] getFingerValues() {
        return fingerValues;
    }

    public float[][] getValues() {
        return fingerValues;
    }

    public void setFingerValues(float[][] values) {
        fingerValues = values;
    }

    public HandConfiguration interpolate(Interpolatable initial, float percentage) {
        float[][] newValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];
        for (int i=0; i<NUM_FINGERS; ++i) {
            for (int j=0; j<NUM_FINGER_TWEAKS; ++j) {
                float totalDifference = fingerValues[i][j] - ((HandConfiguration)(initial)).getValues()[i][j];
                float difference = totalDifference * percentage;
                newValues[i][j] = ((HandConfiguration)(initial)).getValues()[i][j] + difference;
            }
        }

        return new HandConfiguration(newValues);
    }

}
