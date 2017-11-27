package engine;
import java.util.*;
import engine.animation.*;

class KeyFrame<T extends Interpolatable> {
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

        return (T) endResult.interpolate((Interpolatable)initial, animationFunction.run(percentage));
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

public class Timeline<T extends Interpolatable> {

    private T start;
    private ArrayList<KeyFrame<T>> keys = new ArrayList<>();

    public void setStart(T initial) {
        start = initial;
    }

    public void addKeyFrame(T data, float duration) {
        keys.add(new KeyFrame<T>(data, duration));
    }

    public void addKeyFrame(T data, float duration, AnimationFunction fn) {
        keys.add(new KeyFrame<T>(data, duration, fn));
    }


    // T == HandConfiguration
    public T getAnimationState(float t) {
        float elapsed = 0;

        Iterator iterator = keys.iterator();
        KeyFrame<T> currentKey = (KeyFrame<T>) iterator.next(); // key(d1, 1000)
        KeyFrame<T> prevKey = new KeyFrame(start, 0); // d1

        while (iterator.hasNext() && (t > elapsed + currentKey.getDuration())) {
            elapsed += currentKey.getDuration();
            prevKey = currentKey;
            currentKey = (KeyFrame<T>) iterator.next();
        }

        float remaining = t - elapsed;
        return currentKey.getAnimationState(prevKey.getEndResult(), remaining);
    }

}
