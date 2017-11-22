package scene;

// Represents a configuration of a hand

// Basically a Struct to hold the hand configuration
public class HandConfiguration {

    public final static int NUM_FINGERS = 5;
    public final static int NUM_FINGER_TWEAKS = 6;

    private float[][] fingerValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];

    public float[][] getFingerValues() {
        return fingerValues;
    }

}
