package scene.handpositions;
import scene.*;
import engine.*;

public interface HandPosition<T extends Interpolatable<T>> {
    public HandConfiguration getAnimationState(float t);

    public Timeline<T> getTimeline();
}
