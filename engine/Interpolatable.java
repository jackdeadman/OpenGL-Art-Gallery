package engine;

public interface Interpolatable<Z> {
    public Interpolatable<Z> interpolate(Interpolatable<Z> inital, float percentage);
    public Z getValues();
}
