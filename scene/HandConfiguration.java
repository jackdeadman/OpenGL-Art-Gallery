package scene;

// Represents a configuration of a hand

// Basically a Struct to hold the hand configuration
public class HandConfiguration {

    public float finger1Bend = 0.0f;
    public float finger2Bend = 0.0f;
    public float finger3Bend = 0.0f;
    public float finger4Bend = 0.0f;
    public float thumbBend = 0.0f;

    public float upperPalmBend = 0.0f;
    public float handBend = 0.0f;
    public float handRotation = 0.5f;

    public HandConfiguration(
        float finger1Bend,
        float finger2Bend,
        float finger3Bend,
        float finger4Bend,
        float thumbBend,
        float upperPalmBend,
        float handBend,
        float handRotation
    ) {
        this.finger1Bend = finger1Bend;
        this.finger2Bend = finger2Bend;
        this.finger3Bend = finger3Bend;
        this.finger4Bend = finger4Bend;
        this.thumbBend = thumbBend;
        this.upperPalmBend = upperPalmBend;
        this.handBend = handBend;
        this.handRotation = handRotation;
    }

    public HandConfiguration() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

}
