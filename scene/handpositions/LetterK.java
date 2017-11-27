package scene.handpositions;
import engine.*;
import scene.*;
import engine.animation.*;

public class LetterK implements HandPosition<HandConfiguration> {

    public static final float[][] FINGER_VALUES = {
        { 0.8f, 0.50f, 0.16f, 0.0f, 0.06f, 0.18f }, // finger 1
        { 0.8f, 0.63f, 0.12f, 0.0f, 0.06f, 0.18f }, // finger 2
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.15f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.1f } // finger 4
    };

    public static final float[] THUMB_VALUES = { 0.0f, -0.65f, 0.0f, 0.65f, 0.2f };


    private Timeline<HandConfiguration> timeline;

    public LetterK() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 2000);
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 3000);
    }

    public Timeline<HandConfiguration> getTimeline() {
        return timeline;
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
