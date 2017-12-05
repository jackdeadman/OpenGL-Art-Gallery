package engine.animation;

public class AnimationEngine<T extends Interpolatable<T>> {
    /**
     * Class for controlling Animation e.g. play, pause, reset
     */

    // Elapsed Time when paused
    private double pauseTime = 0;
    // Time animation started
    private double startTime = 0;

    // Timeline which is going to be controlled by the engine
    private Timeline<T> timeline;
    private boolean playing = false;

    public void setTimeline(Timeline<T> timeline) {
        this.timeline = timeline;
    }

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    public void startAnimation() {
        playing = true;
        startTime = getSeconds()-pauseTime;
        pauseTime = 0;
    }

    public void pauseAnimation() {
        pauseTime = getElapsedTime();
        playing = false;
    }

    public void resetAnimation() {
        startTime = getSeconds();
        pauseTime = 0;
        // Pause after resetting, don't want playback
        // to continue after resetting
        playing = false;
    }

    public double getElapsedTime() {
        return playing ? getSeconds() - startTime : pauseTime;
    }

    public T getAnimationState() {
        return timeline.getAnimationState((float) (getElapsedTime() * 1000));
    }
}
