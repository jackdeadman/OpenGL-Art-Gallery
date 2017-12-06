package galleryscene.handpositions;
import engine.animation.*;
import galleryscene.*;

public class Love implements HandPosition<HandConfiguration> {
    /**
    * @author Jack Deadman
    */
    public static final float[][] FINGER_VALUES = {
        { 0.0f, 0.06f, 0.05f, 0.0f, 0.0f, 0.04f }, // finger 1
        { 0.88f, 0.61f, 0.17f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.82f, 0.79f, 0.12f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.00f, 0.0f, -0.06f } // finger 4
    };

    public static final float[] THUMB_VALUES = { 0.0f, -0.2f, 0.0f, 0.0f, 0.4f };


    private Timeline<HandConfiguration> timeline;

    public Love() {
        timeline = new Timeline<HandConfiguration>();

        // Default config
        timeline.setStart(new HandConfiguration());

        // To natural do this sign it makes sense to do a fist first
        timeline.addKeyFrame(new HandConfiguration(new float[][] {
            { 0.88f, 0.61f, 0.17f, 0.0f, 0.0f, 0.0f }, // finger 1
            { 0.88f, 0.61f, 0.17f, 0.0f, 0.0f, 0.0f }, // finger 2
            { 0.82f, 0.79f, 0.12f, 0.0f, 0.0f, 0.0f }, // finger 3
            { 0.88f, 0.61f, 0.17f, 0.0f, 0.0f, 0.0f } // finger 4
        }, new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }), 1000);

        // Then open the thist a bit faster
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 1200);
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES, THUMB_VALUES), 3000);

        // Hold Position Two seconds
        // timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 200000);
        // Return to Start
        // timeline.addKeyFrame(new HandConfiguration(), 10000);
    }

    public Timeline<HandConfiguration> getTimeline() {
        return timeline;
    }


    public HandConfiguration getAnimationState(float t) {
        return (HandConfiguration) timeline.getAnimationState(t);
    }

}
