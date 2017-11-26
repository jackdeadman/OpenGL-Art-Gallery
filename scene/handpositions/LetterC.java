package scene.handpositions;
import engine.*;
import scene.*;

public class LetterC implements HandPosition {

    public static final float[][] FINGER_VALUES = {
        { 0.27f, 0.39f, 0.45f, 0.0f, 0.0f, 0.0f }, // finger 1
        { 0.42f, 0.25f, 0.41f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.41f, 0.31f, 0.40f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.30f, 0.44f, 0.30f, 0.0f, 0.0f, 0.0f } // finger 4
    };


    private Timeline timeline;

    public LetterC() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, new float[] { 0.0f, 0.0f, 0.0f, 0.8f, 0.3f }, new float[] { 0.0f, 1.0f }), 4000);
        // Hold Position Two seconds
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 2000);
        // Return to Start
        timeline.addKeyFrame(new HandConfiguration(), 10000);
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
