package engine.animation;

public class AnimationEngine<T extends Interpolatable<T>> {

    private double savedTime = 0;
    private double startTime;
    private Timeline<T> timeline;
    private boolean playing = false;
    private double pauseTime = 0;

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
        playing = false;
    }

    public double getElapsedTime() {
        return playing ? getSeconds() - startTime : pauseTime;
    }

    public T getAnimationState() {
        return timeline.getAnimationState((float) (getElapsedTime() * 1000));
    }
}
