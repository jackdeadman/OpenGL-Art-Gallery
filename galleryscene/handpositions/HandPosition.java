package galleryscene.handpositions;
import galleryscene.*;
import engine.animation.*;

public interface HandPosition<T extends Interpolatable<T>> {
    /**
    * @author Jack Deadman
    */
    public HandConfiguration getAnimationState(float t);

    public Timeline<T> getTimeline();
}
