package engine.animation;

// Interface for an object that can be interpolated e.g a
// hand configuration or simply a number.
public interface Interpolatable<T extends Interpolatable<T>> {
    T interpolate(T inital, float percentage);
}
