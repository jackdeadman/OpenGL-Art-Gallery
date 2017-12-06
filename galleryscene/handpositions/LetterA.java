package galleryscene.handpositions;
import galleryscene.*;
import engine.animation.*;

public class LetterA implements HandPosition<HandConfiguration> {
    /**
    * @author Jack Deadman
    */
    public static final float[][] FINGER_VALUES = {
        { 1.0f, 0.55f, 0.22f, 0.0f, 0.04f, 0.02f }, // finger 1
        { 1.0f, 0.47f, 0.31f, 0.0f, 0.12f, 0.02f }, // finger 2
        { 0.85f, 0.60f, 0.25f, 0.11f, 0.16f, 0.02f }, // finger 3
        { 0.76f, 0.63f, 0.30f, 0.0f, 0.15f, 0.0f } // finger 4
    };

    public static final float[] THUMB_VALUES = { 0.0f, 0.0f, 0.2f, 0.1f, 0.15f };

    private Timeline<HandConfiguration> timeline;

    public LetterA() {
        timeline = new Timeline<HandConfiguration>();

        timeline.setStart(new HandConfiguration());
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 1500);
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 3000);

    }

    public Timeline<HandConfiguration> getTimeline() {
        return timeline;
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
