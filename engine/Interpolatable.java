package engine;

public interface Interpolatable<T extends Interpolatable<T>> {
    public T interpolate(T inital, float percentage);
}
