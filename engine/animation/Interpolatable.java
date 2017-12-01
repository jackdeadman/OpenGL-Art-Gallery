package engine.animation;

public interface Interpolatable<T extends Interpolatable<T>> {
    T interpolate(T inital, float percentage);
}
