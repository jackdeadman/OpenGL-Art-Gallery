package scene.handpositions;
import engine.*;
import scene.*;

public class Love implements HandPosition {

    public static final float[][] FINGER_VALUES = {
        { 0.0f, 0.06f, 0.05f, 0.0f, 0.0f, 0.04f }, // finger 1
        { 0.88f, 0.61f, 0.17f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.82f, 0.79f, 0.12f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.00f, 0.0f, -0.06f } // finger 4
    };


    private Timeline timeline;

    public Love() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, new float[] { 0.0f, -0.2f, 0.0f, 0.0f, 0.4f }), 4000);
        // Hold Position Two seconds
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 200000);
        // Return to Start
        timeline.addKeyFrame(new HandConfiguration(), 10000);
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
