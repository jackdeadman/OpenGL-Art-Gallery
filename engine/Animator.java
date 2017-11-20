package engine;

import java.util.collections.*;

public class Animator {

    private ArrayList<Keyframes> keyframes = new ArrayList<>();
    private Keyframe currentKeyframe;
    private float totalDuration = 0;

    public void addKeyframe(float duration, Interpolatable config, AnimationCurve curve) {
        keyframes.add(new Keyframe(totalDuration, duration, config, curve));
    }

    public Interpolatable getAnimationState(float time) {
        Keyframe closest = getClosestFrame(time);
        return closest.interpolate(time);
    }

    private Keyframe getClosestFrame(float time) {
        Keyframe closest = keyframes.get(0);

        for (Keyframe key : keyframes) {
            if (key.getStartTime() < closest.getStartTime()) {
                closest = key;
            }
        }

        return closest;
    }



}

///*

Animator animator = new Animator<HandConfiguration>();

AnimationCurve curve = new BezierCurve();
animator.addKeyframe(2000, configuration1, curve));
animator.addKeyframe(4000, configuration1, curve));


animator.getAnimationState(time);

*/
