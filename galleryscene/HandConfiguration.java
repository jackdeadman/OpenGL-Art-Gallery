package galleryscene;
import engine.animation.*;
// Represents a configuration of a hand

public class HandConfiguration implements Interpolatable<HandConfiguration> {

    public final static int NUM_FINGERS = 4;
    public final static int NUM_FINGER_TWEAKS = 6;

    private float[][] fingerValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];
    private float[] thumbValues = new float[5]; // turn(x,y,x), lower joint, middle joint
    private float[] wristValues = new float[2]; // X, Y rotate

    public HandConfiguration() {
    }

    public HandConfiguration(float[][] values) {
        fingerValues = values;
    }


    public HandConfiguration(float[][] values, float[] thumbValues) {
        fingerValues = values;
        this.thumbValues = thumbValues;
    }

    public HandConfiguration(float[][] values, float[] thumbValues, float[] wristValues) {
        fingerValues = values;
        this.thumbValues = thumbValues;
        this.wristValues = wristValues;
    }


    public float[][] getFingerValues() {
        return fingerValues;
    }

    public float[][] getValues() {
        return fingerValues;
    }

    public float[] getThumbValues() {
        return thumbValues;
    }

    public void setThumbValues(float[] thumbValues) {
        this.thumbValues = thumbValues;
    }

    public float[] getWristValues() {
        return wristValues;
    }

    public void setWristValues(float[] wristValues) {
        this.wristValues = wristValues;
    }

    public void setFingerValues(float[][] values) {
        fingerValues = values;
    }

    private float linearInterpolation(float start, float end, float percentage) {
        float delta = end - start;
        return start + (delta * percentage);
    }

    // Interpolates between two Hand configurations
    public HandConfiguration interpolate(HandConfiguration initial, float percentage) {
        HandConfiguration initialHandConfiguration = (HandConfiguration)initial;

        float[][] newFingerValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];
        for (int i=0; i<NUM_FINGERS; ++i) {
            for (int j=0; j<NUM_FINGER_TWEAKS; ++j) {
                float end = fingerValues[i][j];
                float start = initialHandConfiguration.getValues()[i][j];
                newFingerValues[i][j] = linearInterpolation(start, end, percentage);
            }
        }

        float[] newThumbValues = new float[thumbValues.length];
        for (int i=0; i<thumbValues.length; ++i) {
            float end = thumbValues[i];
            newThumbValues[i] = linearInterpolation(
                initialHandConfiguration.getThumbValues()[i], end, percentage
            );
        }


        float[] newWristValues = new float[wristValues.length];
        for (int i=0; i<wristValues.length; ++i) {
            float end = wristValues[i];
            newWristValues[i] = linearInterpolation(
                initialHandConfiguration.getWristValues()[i], end, percentage
            );
        }

        HandConfiguration newConfig = new HandConfiguration();
        newConfig.setFingerValues(newFingerValues);
        newConfig.setThumbValues(newThumbValues);
        newConfig.setWristValues(newWristValues);

        return newConfig;
    }
/*
    public HandConfiguration interpolate2(Interpolatable initial, float percentage) {
        float[][] newValues = new float[NUM_FINGERS][NUM_FINGER_TWEAKS];
        for (int i=0; i<NUM_FINGERS; ++i) {
            for (int j=0; j<NUM_FINGER_TWEAKS; ++j) {
                float totalDifference = fingerValues[i][j] - ((HandConfiguration)(initial)).getValues()[i][j];
                float difference = totalDifference * percentage;
                newValues[i][j] = ((HandConfiguration)(initial)).getValues()[i][j] + difference;
            }
        }
        return new HandConfiguration(newValues);
    }*/

}
