package engine;
import java.util.*;

class KeyFrame<T extends Interpolatable> {
    private float duration;
    private T endResult;
    // private Object animationFunction;

    public KeyFrame(T endResult, float duration) {
        this.duration = duration;
        this.endResult = endResult;
    }

    public T getAnimationState(T initial, float time) {
        float percentage = Math.max(0, Math.min(1, time / duration));
        return (T)endResult.interpolate((Interpolatable)initial, percentage);
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

        float elapsed = 0;

        Iterator iterator = keys.iterator();
        KeyFrame<T> currentKey = (KeyFrame<T>) iterator.next();
        T prev = start;

        while (iterator.hasNext() && (t < elapsed + currentKey.getDuration())) {
            prev = currentKey.getEndResult();
            currentKey = (KeyFrame<T>) iterator.next();
            elapsed += currentKey.getDuration();
        }

        float remaining = t - elapsed;
        return currentKey.getAnimationState(prev, remaining);
    }

}
