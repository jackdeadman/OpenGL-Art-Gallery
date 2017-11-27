package scene.handpositions;
import scene.*;
import engine.*;

public interface HandPosition<T extends Interpolatable> {
    public HandConfiguration getAnimationState(float t);

    public Timeline<T> getTimeline();
}
