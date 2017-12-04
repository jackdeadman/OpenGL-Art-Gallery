package engine.animation;

// Interface for a function which can
// alter how can animation changes over time
public interface AnimationFunction {
    public float run(float t);
}
