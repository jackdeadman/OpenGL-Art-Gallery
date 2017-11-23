package scene.handpositions;

public class Neutral implements HandPosition {
    public static final float[][] CONFIG = {
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 1
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f } // finger 4
    };

    public float[][] getAnimationState(float t) {
        return CONFIG;
    }
}
