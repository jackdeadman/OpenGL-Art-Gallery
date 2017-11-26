package scene.handpositions;
import engine.*;
import scene.*;

public class LetterK implements HandPosition {

    public static final float[][] FINGER_VALUES = {
        { 1.0f, 0.40f, 0.16f, 0.0f, 0.06f, 0.18f }, // finger 1
        { 0.69f, 0.73f, 0.12f, 0.0f, 0.06f, 0.18f }, // finger 2
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.15f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.1f } // finger 4
    };


    private Timeline timeline;

    public LetterK() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, new float[] { 0.0f, -0.65f, 0.0f, 0.65f, 0.2f }), 4000);
        // Hold Position Two seconds
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 200000);
        // Return to Start
        timeline.addKeyFrame(new HandConfiguration(), 10000);
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
