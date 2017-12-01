package galleryscene.handpositions;
import engine.animation.*;
import galleryscene.*;

public class LetterC implements HandPosition<HandConfiguration> {

    public static final float[][] FINGER_VALUES = {
        { 0.27f, 0.39f, 0.45f, 0.0f, 0.0f, 0.0f }, // finger 1
        { 0.42f, 0.25f, 0.41f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.41f, 0.31f, 0.40f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.30f, 0.44f, 0.30f, 0.0f, 0.0f, 0.0f } // finger 4
    };

    public static final float[] THUMB_VALUES = { 0.0f, 0.0f, 0.0f, 0.8f, 0.3f };
    public static final float[] HAND_VALUES = { 0.0f, 1.0f };


    private Timeline<HandConfiguration> timeline;

    public LetterC() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES, HAND_VALUES), 1000);
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES, HAND_VALUES), 3000);
    }

    public Timeline<HandConfiguration> getTimeline() {
        return timeline;
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
