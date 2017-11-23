package scene.handpositions;
import engine.*;
import scene.*;


public class LetterD implements HandPosition {

    public static final float[][] CONFIG = {
        { 0.7f, 0.4f, 0.2f, 0.49f, 0.14f, 0.2f }, // finger 1
        { 0.84f, 0.57f, 0.09f, 0.15f, 0.24f, 0.2f }, // finger 2
        { 0.73f, 0.75f, 0.11f, 0.22f, 0.25f, 0.25f }, // finger 3
        { 0.09f, 0.0f, 0.13f, 0.06f, 0.0f, 0.09f } // finger 4
    };

    private Timeline timeline;

    public LetterD() {
        timeline = new Timeline<HandConfiguration>();
        timeline.setStart(new HandConfiguration(Neutral.CONFIG));
        timeline.addKeyFrame(
            new HandConfiguration(new float[][] {
                    { 0.7f, 0.4f, 0.2f, 0.49f, 0.14f, 0.2f }, // finger 1
                    { 0.84f, 0.57f, 0.09f, 0.15f, 0.24f, 0.2f }, // finger 2
                    { 0.73f, 0.75f, 0.11f, 0.22f, 0.25f, 0.25f }, // finger 3
                    { 0.09f, 0.0f, 0.13f, 0.06f, 0.0f, 0.09f }
            }), 5000);


        timeline.addKeyFrame(
            new HandConfiguration(Neutral.CONFIG), 10000);
    }


    public float[][] getAnimationState(float t) {
        return (float[][])timeline.getAnimationState(t).getValues();
    }
}
