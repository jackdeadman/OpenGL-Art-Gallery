package engine;
import java.util.*;
import engine.animation.*;

class KeyFrame<T extends Interpolatable> {
    private float duration;
    private T endResult;
    // private Object animationFunction;

    public KeyFrame(T endResult, float duration) {
        this.duration = duration;
        this.endResult = endResult;
    }

    public T getAnimationState(T initial, float time, AnimationFunction function) {
        float percentage = Math.max(0, Math.min(1, function.run(time) / duration));
        return (T)endResult.interpolate((Interpolatable)initial, percentage);
    }

    public T getAnimationState(T initial, float time) {
        return getAnimationState(initial, time, AnimationFunctions.linear());
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


    // T == HandConfiguration
    public T getAnimationState(float t) {
        System.out.println(t);

        // [key(d1, 1000), key(d2, 2000)]

        float elapsed = 0;

        Iterator iterator = keys.iterator();
        KeyFrame<T> currentKey = (KeyFrame<T>) iterator.next(); // key(d1, 1000)
        KeyFrame<T> prevKey = new KeyFrame(start, 0); // d1

        while (iterator.hasNext() && (t > elapsed + currentKey.getDuration())) {
            elapsed += currentKey.getDuration();
            prevKey = currentKey;
            currentKey = (KeyFrame<T>) iterator.next();
        }

        // if (t > currentKey.getDuration()) {
        //     elapsed = currentKey.getDuration();
        //     prevKey = currentKey;
        //     currentKey = (KeyFrame<T>) iterator.next();
        // }
        //
        // while (t > pre)


        float remaining = t - elapsed;
        System.out.println(prevKey);
        System.out.println(remaining);
        return currentKey.getAnimationState(prevKey.getEndResult(), remaining);
    }

}
