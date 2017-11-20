package engine;

public class Keyframe<T> {

    private float duration;
    private Interpolatable<T> config;
    private AnimationCurve curve;

    public Keyframe(float start, float duration, Interpolatable<T> config, AnimationCurve curve) {
        this.start = start;
        this.duration = duration;
        this.config = config;
        this.curve = curve;
    }

    public Interpolatable interpolate(float time) {
        float amountPlayed = start - time;
        float percentagePlayed = amountPlayed / duration;
        return config.interpolate(percentagePlayed);
    }

}


/*

Animator animator = new Animator();
AnimationCurve curve = new BezierCurve(new Vec(0,0,0));
animator.addKeyframe(2000, configuration1, curve);
animator.addKeyframe(4000, configuration1, curve);


animator.getAnimationState(time);

*/
