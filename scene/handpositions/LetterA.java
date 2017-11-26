package scene.handpositions;
import engine.*;
import scene.*;

public class LetterA implements HandPosition {

    public static final float[][] FINGER_VALUES = {
        { 1.0f, 0.55f, 0.22f, 0.0f, 0.04f, 0.02f }, // finger 1
        { 1.0f, 0.47f, 0.31f, 0.0f, 0.12f, 0.02f }, // finger 2
        { 0.85f, 0.60f, 0.25f, 0.11f, 0.16f, 0.02f }, // finger 3
        { 0.76f, 0.63f, 0.30f, 0.0f, 0.15f, 0.0f } // finger 4
    };


    private Timeline timeline;

    public LetterA() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, new float[] { 1.0f, 1.0f }), 4000);
        // Hold Position Two seconds
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 200000);
        // Return to Start
        timeline.addKeyFrame(new HandConfiguration(), 10000);
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
