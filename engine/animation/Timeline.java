package engine.animation;
import java.util.*;

/**
 * Flexible animation framework
 * Allows an animation to be created with any Interpolatable object e.g a hand configuration.
 * Animation are created by simply adding KeyFrames.
 * @param <T>
 */
class KeyFrame<T extends Interpolatable<T>> {
    /**
    * @author Jack Deadman
    */
    private float duration;
    private T endResult;
    private AnimationFunction animationFunction;

    /**
     * A point in time which T will be interpolated to.
     * @param endResult The state at the end of interpolation
     * @param duration How long the interpolation should take
     * @param fn An animation function
     */
    public KeyFrame(T endResult, float duration, AnimationFunction fn) {
        this.duration = duration;
        this.endResult = endResult;
        this.animationFunction = fn;
    }

    public KeyFrame(T endResult, float duration) {
        this(endResult, duration, AnimationFunctions.cos());
    }

    /**
     * Get the state of T for a time t in the animation
     * @param initial The starting point
     * @param time how far into the animation
     * @param function The animation function being used
     * @return T the state at time t
     */
    public T getAnimationState(T initial, float time, AnimationFunction function) {
        float percentage = Math.max(0, Math.min(1, time / duration));

        return (T) endResult.interpolate(initial, animationFunction.run(percentage));
    }

    public T getAnimationState(T initial, float time) {
        return getAnimationState(initial, time, animationFunction);
    }

    public float getDuration() {
        return duration;
    }

    public T getEndResult() {
        return endResult;
    }
}

public class Timeline<T extends Interpolatable<T>> {

    private T start;
    private ArrayList<KeyFrame<T>> keys = new ArrayList<>();

    /**
     * The initial configuration at the start of the animation. THIS MUST BE SET.
     * @param initial
     */
    public void setStart(T initial) {
        start = initial;
    }

    /**
     * Get the keyframes that have been set in this animation.
     * @return An arraylist of the keyframes.
     */
    public ArrayList<KeyFrame<T>> getKeys() {
        return keys;
    }

    /**
     * Set all the keyframes for the animation at once.
     * @param keys The keyframes to set.
     */
    public void setKeys(ArrayList<KeyFrame<T>> keys) {
        this.keys = keys;
    }

    /**
     * Add a new keyframe at the end of the animation
     * @param data The data to interpolate to
     * @param duration How long the interpolation should take.
     */
    public void addKeyFrame(T data, float duration) {
        keys.add(new KeyFrame<T>(data, duration));
    }

    public void addKeyFrame(T data, float duration, AnimationFunction fn) {
        keys.add(new KeyFrame<T>(data, duration, fn));
    }

    /**
     * Extend a timeline with another timeline.
     * @param timetine
     */
    public void extend(Timeline<T> timetine) {
        keys.addAll(new ArrayList<KeyFrame<T>>(timetine.keys));
    }


    /**
     * Get the state of the animation at time t
     * @param t the time
     * @return the state
     */
    public T getAnimationState(float t) {
        float elapsed = 0;

        Iterator<KeyFrame<T>> iterator = keys.iterator();
        KeyFrame<T> currentKey = iterator.next();
        KeyFrame<T> prevKey = new KeyFrame<T>(start, 0);

        // Find the keyframe that we are current on at this time.
        while (iterator.hasNext() && (t > elapsed + currentKey.getDuration())) {
            elapsed += currentKey.getDuration();
            prevKey = currentKey;
            currentKey = (KeyFrame<T>) iterator.next();
        }

        float remaining = t - elapsed;
        return currentKey.getAnimationState(prevKey.getEndResult(), remaining);
    }

}
