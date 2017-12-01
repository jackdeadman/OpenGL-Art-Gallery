package engine.animation;
import java.util.*;

class KeyFrame<T extends Interpolatable<T>> {
    private float duration;
    private T endResult;
    private AnimationFunction animationFunction;


    public KeyFrame(T endResult, float duration, AnimationFunction fn) {
        this.duration = duration;
        this.endResult = endResult;
        this.animationFunction = fn;
    }

    public KeyFrame(T endResult, float duration) {
        this(endResult, duration, AnimationFunctions.cos());
    }

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

    public void setStart(T initial) {
        start = initial;
    }

    public ArrayList<KeyFrame<T>> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<KeyFrame<T>> keys) {
        this.keys = keys;
    }

    public void addKeyFrame(T data, float duration) {
        keys.add(new KeyFrame<T>(data, duration));
    }

    public void addKeyFrame(T data, float duration, AnimationFunction fn) {
        keys.add(new KeyFrame<T>(data, duration, fn));
    }

    public void extend(Timeline<T> timetime) {
        keys.addAll(new ArrayList<KeyFrame<T>>(timetime.keys));
    }


    // T == HandConfiguration
    public T getAnimationState(float t) {
        float elapsed = 0;

        Iterator<KeyFrame<T>> iterator = keys.iterator();
        KeyFrame<T> currentKey = iterator.next();
        KeyFrame<T> prevKey = new KeyFrame<T>(start, 0); // d1

        while (iterator.hasNext() && (t > elapsed + currentKey.getDuration())) {
            elapsed += currentKey.getDuration();
            prevKey = currentKey;
            currentKey = (KeyFrame<T>) iterator.next();
        }

        float remaining = t - elapsed;
        return currentKey.getAnimationState(prevKey.getEndResult(), remaining);
    }

}
